package com.mycompany.ehr.dtc.model;

import java.time.LocalDateTime;

public class User {
    private final int userId;
    private final LocalDateTime createdAt;
    private String username;
    private String passwordHash;
    private String phone;
    private String fullName;
    private boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime updatedAt;

    public User(int userId, String username, String passwordHash, 
                String phone, String fullName, boolean isActive, 
                LocalDateTime lastLogin, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.fullName = fullName;
        this.isActive = isActive;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(String username, String passwordHash,  String phone, String fullName) {
        this(0, username, passwordHash, phone, fullName, true, null, LocalDateTime.now(), LocalDateTime.now());
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getPhone() { return phone; }
    public String getFullName() { return fullName; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setActive(boolean active) { isActive = active; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", fullName='" + fullName + '\'' +
                ", isActive=" + isActive +
                ", lastLogin=" + lastLogin +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}