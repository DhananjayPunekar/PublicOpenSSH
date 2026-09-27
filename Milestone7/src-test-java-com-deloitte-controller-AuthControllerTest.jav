package com.deloitte.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.deloitte.dto.LoginForm;
import com.deloitte.dto.SignUpForm;
import com.deloitte.exception.DuplicateEmailException;
import com.deloitte.exception.InvalidCredentialsException;
import com.deloitte.model.User;
import com.deloitte.service.UserService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Runs the controller without Tomcat; validation (@Valid) still works
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    private User user(String role) {
        User u = new User();
        u.setUserId(1L);
        u.setEmail("alice@email.com");
        u.setPassword("abc123");
        u.setRole(role);
        return u;
    }

    // ---------- Sign Up ----------
    @Test
    @DisplayName("GET /signup shows the sign-up page with an empty form")
    void getSignup_showsForm() throws Exception {
        mockMvc.perform(get("/signup"))
               .andExpect(status().isOk())
               .andExpect(view().name("signup"))
               .andExpect(model().attributeExists("signupForm"));
    }

    @Test
    @DisplayName("POST /signup with empty fields redisplays the form with errors")
    void postSignup_invalid_showsErrors() throws Exception {
        mockMvc.perform(post("/signup").param("email", "").param("password", "").param("role", ""))
               .andExpect(view().name("signup"))
               .andExpect(model().attributeHasFieldErrors("signupForm", "email", "password", "role"));

        verify(userService, never()).register(any(SignUpForm.class));
    }

    @Test
    @DisplayName("POST /signup with valid data registers and redirects to login")
    void postSignup_valid_redirectsToLogin() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("email", "alice@email.com")
                        .param("password", "abc123")
                        .param("role", "USER"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login"))
               .andExpect(flash().attributeExists("successMessage"));

        verify(userService).register(any(SignUpForm.class));
    }

    @Test
    @DisplayName("POST /signup with an existing email shows an error on the email field")
    void postSignup_duplicateEmail_showsEmailError() throws Exception {
        doThrow(new DuplicateEmailException("alice@email.com"))
                .when(userService).register(any(SignUpForm.class));

        mockMvc.perform(post("/signup")
                        .param("email", "alice@email.com")
                        .param("password", "abc123")
                        .param("role", "USER"))
               .andExpect(view().name("signup"))
               .andExpect(model().attributeHasFieldErrors("signupForm", "email"));
    }

    // ---------- Login ----------
    @Test
    @DisplayName("GET /login shows the login page")
    void getLogin_showsForm() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"))
               .andExpect(model().attributeExists("loginForm"));
    }

    @Test
    @DisplayName("POST /login with empty fields redisplays the form with errors")
    void postLogin_invalid_showsErrors() throws Exception {
        mockMvc.perform(post("/login").param("email", "").param("password", ""))
               .andExpect(view().name("login"))
               .andExpect(model().attributeHasFieldErrors("loginForm", "email", "password"));

        verify(userService, never()).authenticate(any(LoginForm.class));
    }

    @Test
    @DisplayName("USER login goes to the user dashboard and is stored in the session without a password")
    void postLogin_user_redirectsToUserDashboard() throws Exception {
        when(userService.authenticate(any(LoginForm.class))).thenReturn(user("USER"));

        MvcResult result = mockMvc.perform(post("/login")
                        .param("email", "alice@email.com")
                        .param("password", "abc123"))
               .andExpect(redirectedUrl("/user/dashboard"))
               .andReturn();

        User inSession = (User) result.getRequest().getSession().getAttribute("loggedInUser");
        assertNotNull(inSession);
        assertNull(inSession.getPassword(), "Password must not be kept in the session");
    }

    @Test
    @DisplayName("ADMIN login goes to the admin dashboard")
    void postLogin_admin_redirectsToAdminDashboard() throws Exception {
        when(userService.authenticate(any(LoginForm.class))).thenReturn(user("ADMIN"));

        mockMvc.perform(post("/login")
                        .param("email", "admin@email.com")
                        .param("password", "abc123"))
               .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    @DisplayName("Wrong credentials show 'Invalid email or password'")
    void postLogin_wrongPassword_showsError() throws Exception {
        when(userService.authenticate(any(LoginForm.class)))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/login")
                        .param("email", "alice@email.com")
                        .param("password", "wrong1"))
               .andExpect(view().name("login"))
               .andExpect(model().attributeExists("loginError"));
    }

    @Test
    @DisplayName("After login, a guest who clicked Book is sent back to that flight")
    void postLogin_withPendingFlight_redirectsToBooking() throws Exception {
        when(userService.authenticate(any(LoginForm.class))).thenReturn(user("USER"));
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("pendingFlightId", 16L);

        mockMvc.perform(post("/login").session(session)
                        .param("email", "alice@email.com")
                        .param("password", "abc123"))
               .andExpect(redirectedUrl("/booking/new?flightId=16"));
    }

    @Test
    @DisplayName("GET /logout ends the session and returns home")
    void logout_redirectsHome() throws Exception {
        mockMvc.perform(get("/logout"))
               .andExpect(redirectedUrl("/home"));
    }
}
