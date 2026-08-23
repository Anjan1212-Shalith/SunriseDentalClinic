package dao;

import db.DBconnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Data Access Object for Users and Staff
public class UserDAO {

    // User login validation
    public User login(String username, String password) {
        String sql = "SELECT user_id, username, password, full_name, role, contact_no, created_at FROM users WHERE username = ? AND password = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return null;
            
            pst.setString(1, username);
            pst.setString(2, password);
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    user.setContactNo(rs.getString("contact_no"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("User login error: " + e.getMessage());
        }
        return null;
    }

    // Register a new staff user
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, full_name, role, contact_no) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (con == null) return false;

            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getFullName());
            pst.setString(4, user.getRole() != null ? user.getRole() : "Receptionist");
            pst.setString(5, user.getContactNo());

            int affected = pst.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Register user error: " + e.getMessage());
        }
        return false;
    }

    // Update user details
    public boolean updateUser(User user) {
        String sql;
        boolean updatePassword = user.getPassword() != null && !user.getPassword().isEmpty();
        
        if (updatePassword) {
            sql = "UPDATE users SET full_name = ?, role = ?, contact_no = ?, password = ? WHERE user_id = ?";
        } else {
            sql = "UPDATE users SET full_name = ?, role = ?, contact_no = ? WHERE user_id = ?";
        }

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            pst.setString(1, user.getFullName());
            pst.setString(2, user.getRole());
            pst.setString(3, user.getContactNo());
            
            if (updatePassword) {
                pst.setString(4, user.getPassword());
                pst.setInt(5, user.getUserId());
            } else {
                pst.setInt(4, user.getUserId());
            }

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update user error: " + e.getMessage());
        }
        return false;
    }

    // Delete user by ID
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            pst.setInt(1, userId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete user error: " + e.getMessage());
        }
        return false;
    }

    // Check if username already exists
    public boolean usernameExists(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return false;

            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Check username error: " + e.getMessage());
        }
        return false;
    }

    // Get all registered users
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, username, password, full_name, role, contact_no, created_at FROM users ORDER BY role ASC, full_name ASC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            
            if (con == null) return list;

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                user.setContactNo(rs.getString("contact_no"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Get all users error: " + e.getMessage());
        }
        return list;
    }

    // Search users by keyword
    public List<User> searchUsers(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, username, password, full_name, role, contact_no, created_at FROM users WHERE username LIKE ? OR full_name LIKE ? OR role LIKE ? ORDER BY full_name ASC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            if (con == null) return list;

            String pattern = "%" + keyword + "%";
            pst.setString(1, pattern);
            pst.setString(2, pattern);
            pst.setString(3, pattern);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    user.setContactNo(rs.getString("contact_no"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            System.out.println("Search users error: " + e.getMessage());
        }
        return list;
    }
}
