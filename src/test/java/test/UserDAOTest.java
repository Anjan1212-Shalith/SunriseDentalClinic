package test;

import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated Unit Test Suite for UserDAO & Authentication (Task C)
 * Covers: Valid logins, invalid credentials, duplicate checks, and registration
 */
public class UserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    @DisplayName("TC-AUTH-01: Valid Admin login credentials should return User object")
    public void testValidAdminLoginShouldReturnUser() {
        User user = userDAO.login("admin", "admin123");
        assertNotNull(user, "Valid admin login should not return null");
        assertEquals("admin", user.getUsername(), "Username should match 'admin'");
        assertEquals("Admin", user.getRole(), "User role should be 'Admin'");
    }

    @Test
    @DisplayName("TC-AUTH-02: Valid Receptionist login credentials should return User object")
    public void testValidReceptionistLoginShouldReturnUser() {
        User user = userDAO.login("receptionist", "staff123");
        assertNotNull(user, "Valid receptionist login should not return null");
        assertEquals("Receptionist", user.getRole(), "Role should match 'Receptionist'");
    }

    @Test
    @DisplayName("TC-AUTH-03: Invalid password with valid username should return null")
    public void testInvalidPasswordShouldReturnNull() {
        User user = userDAO.login("admin", "wrong_password_999");
        assertNull(user, "Login with invalid password must fail and return null");
    }

    @Test
    @DisplayName("TC-AUTH-04: Non-existent username should return null")
    public void testInvalidUsernameShouldReturnNull() {
        User user = userDAO.login("non_existent_user_xyz", "admin123");
        assertNull(user, "Login with non-existent username must fail and return null");
    }

    @Test
    @DisplayName("TC-AUTH-05: Empty username and password strings should return null")
    public void testEmptyCredentialsShouldReturnNull() {
        User user = userDAO.login("", "");
        assertNull(user, "Login with empty credentials must return null");
    }

    @Test
    @DisplayName("TC-AUTH-06: Existing username check should return true for registered user")
    public void testUsernameExistsForRegisteredUser() {
        boolean exists = userDAO.usernameExists("admin");
        assertTrue(exists, "usernameExists() must return true for 'admin'");
    }

    @Test
    @DisplayName("TC-AUTH-07: Unregistered username check should return false")
    public void testUsernameExistsForUnregisteredUser() {
        boolean exists = userDAO.usernameExists("random_ghost_user_123");
        assertFalse(exists, "usernameExists() must return false for unregistered username");
    }
}
