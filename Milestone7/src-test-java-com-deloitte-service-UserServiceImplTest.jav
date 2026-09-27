package com.deloitte.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import com.deloitte.dao.UserDao;
import com.deloitte.dto.LoginForm;
import com.deloitte.dto.SignUpForm;
import com.deloitte.exception.DuplicateEmailException;
import com.deloitte.exception.InvalidCredentialsException;
import com.deloitte.model.User;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;              // fake DAO: no database needed

    @InjectMocks
    private UserServiceImpl userService;  // real service, with the fake DAO injected

    // ---------- helpers ----------
    private SignUpForm signUpForm(String email, String password, String role) {
        SignUpForm f = new SignUpForm();
        f.setEmail(email);
        f.setPassword(password);
        f.setRole(role);
        return f;
    }

    private LoginForm loginForm(String email, String password) {
        LoginForm f = new LoginForm();
        f.setEmail(email);
        f.setPassword(password);
        return f;
    }

    private User user(String email, String password, String role) {
        User u = new User();
        u.setUserId(1L);
        u.setEmail(email);
        u.setPassword(password);
        u.setRole(role);
        return u;
    }

    // ---------- Sign Up ----------
    @Test
    @DisplayName("Register saves a new user with a trimmed, lowercased email")
    void register_newEmail_savesUser() {
        when(userDao.existsByEmail("alice@email.com")).thenReturn(false);

        userService.register(signUpForm("  Alice@Email.com ", "abc123", "USER"));

        ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(saved.capture());
        assertEquals("alice@email.com", saved.getValue().getEmail());
        assertEquals("abc123", saved.getValue().getPassword());
        assertEquals("USER", saved.getValue().getRole());
    }

    @Test
    @DisplayName("Register rejects an email that already exists and saves nothing")
    void register_duplicateEmail_throwsAndDoesNotSave() {
        when(userDao.existsByEmail("alice@email.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class,
                () -> userService.register(signUpForm("alice@email.com", "abc123", "USER")));

        verify(userDao, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Register turns a DB unique-constraint error into DuplicateEmailException")
    void register_dbDuplicate_throwsDuplicateEmail() {
        when(userDao.existsByEmail("alice@email.com")).thenReturn(false);
        doThrow(new DuplicateKeyException("duplicate")).when(userDao).save(any(User.class));

        assertThrows(DuplicateEmailException.class,
                () -> userService.register(signUpForm("alice@email.com", "abc123", "USER")));
    }

    // ---------- Login ----------
    @Test
    @DisplayName("Login succeeds with the correct email and password")
    void authenticate_correctCredentials_returnsUser() {
        when(userDao.findByEmail("alice@email.com"))
                .thenReturn(user("alice@email.com", "abc123", "USER"));

        User result = userService.authenticate(loginForm("alice@email.com", "abc123"));

        assertNotNull(result);
        assertEquals("alice@email.com", result.getEmail());
        assertEquals("USER", result.getRole());
    }

    @Test
    @DisplayName("Login fails with the wrong password")
    void authenticate_wrongPassword_throws() {
        when(userDao.findByEmail("alice@email.com"))
                .thenReturn(user("alice@email.com", "abc123", "USER"));

        assertThrows(InvalidCredentialsException.class,
                () -> userService.authenticate(loginForm("alice@email.com", "wrong1")));
    }

    @Test
    @DisplayName("Login fails for an email that isn't registered")
    void authenticate_unknownEmail_throws() {
        when(userDao.findByEmail("nobody@email.com")).thenReturn(null);

        assertThrows(InvalidCredentialsException.class,
                () -> userService.authenticate(loginForm("nobody@email.com", "abc123")));
    }
}
