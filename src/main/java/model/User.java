package model;

import java.sql.Timestamp;

/**
 * User Entity POJO representing staff and administrators
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private String contactNo;
    private Timestamp createdAt;

    public User() {
    }

    public User(int userId, String username, String password, String fullName, String role, String contactNo) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.contactNo = contactNo;
    }

    public User(String username, String password, String fullName, String role, String contactNo) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.contactNo = contactNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return fullName + " (" + role + ")";
    }
}
