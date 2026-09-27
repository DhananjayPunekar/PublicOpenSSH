package com.deloitte.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FormValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    // Number of errors on one field
    private <T> long errorsOn(T form, String field) {
        return validator.validate(form).stream()
                .filter(v -> v.getPropertyPath().toString().equals(field))
                .count();
    }

    private SignUpForm signUp(String email, String password, String role) {
        SignUpForm f = new SignUpForm();
        f.setEmail(email);
        f.setPassword(password);
        f.setRole(role);
        return f;
    }

    private LoginForm login(String email, String password) {
        LoginForm f = new LoginForm();
        f.setEmail(email);
        f.setPassword(password);
        return f;
    }

    // ---------- Sign Up ----------
    @Test
    @DisplayName("Valid sign-up form has no errors")
    void signUp_valid_noErrors() {
        Set<ConstraintViolation<SignUpForm>> v = validator.validate(signUp("alice@email.com", "abc123", "USER"));
        assertTrue(v.isEmpty(), () -> "Unexpected errors: "
                + v.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
    }

    @Test
    @DisplayName("Empty sign-up form shows exactly ONE error per field")
    void signUp_empty_oneErrorPerField() {
        SignUpForm f = signUp("", "", "");
        assertEquals(1, errorsOn(f, "email"));
        assertEquals(1, errorsOn(f, "password"));
        assertEquals(1, errorsOn(f, "role"));
    }

    @Test
    @DisplayName("Badly formatted email is rejected")
    void signUp_invalidEmail_rejected() {
        assertEquals(1, errorsOn(signUp("not-an-email", "abc123", "USER"), "email"));
    }

    @Test
    @DisplayName("Password without a number is rejected")
    void signUp_passwordWithoutDigit_rejected() {
        assertEquals(1, errorsOn(signUp("alice@email.com", "abcdef", "USER"), "password"));
    }

    @Test
    @DisplayName("Password without a letter is rejected")
    void signUp_passwordWithoutLetter_rejected() {
        assertEquals(1, errorsOn(signUp("alice@email.com", "123456", "USER"), "password"));
    }

    @Test
    @DisplayName("Password shorter than 6 characters is rejected")
    void signUp_shortPassword_rejected() {
        assertEquals(1, errorsOn(signUp("alice@email.com", "ab1", "USER"), "password"));
    }

    @Test
    @DisplayName("Role other than USER/ADMIN is rejected")
    void signUp_invalidRole_rejected() {
        assertEquals(1, errorsOn(signUp("alice@email.com", "abc123", "SUPERUSER"), "role"));
    }

    // ---------- Login ----------
    @Test
    @DisplayName("Valid login form has no errors")
    void login_valid_noErrors() {
        assertTrue(validator.validate(login("alice@email.com", "abc123")).isEmpty());
    }

    @Test
    @DisplayName("Empty login form shows errors on email and password")
    void login_empty_errors() {
        LoginForm f = login("", "");
        assertEquals(1, errorsOn(f, "email"));
        assertEquals(1, errorsOn(f, "password"));
    }

    @Test
    @DisplayName("Login with badly formatted email is rejected")
    void login_invalidEmail_rejected() {
        assertEquals(1, errorsOn(login("alice", "abc123"), "email"));
    }
}
