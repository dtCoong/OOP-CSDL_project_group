package com.mycompany.ehr.model;

import java.time.LocalDateTime;

public class User {
    private int user_id;
    private String username;
    private String password_hash;
    private String email;
    private String phone;
    private String full_name;
    private boolean is_active;
    private LocalDateTime last_login;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public User() {}
        public User(int user_id, String username, String password_hash, String email, 
                String phone, String full_name, boolean is_active, 
                LocalDateTime last_login, LocalDateTime created_at, LocalDateTime updated_at) {
        this.user_id = user_id;
        this.username = username;
        this.password_hash = password_hash;
        this.email = email;
        this.phone = phone;
        this.full_name = full_name;
        this.is_active = is_active;
        this.last_login = last_login;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return password_hash; }
    public void setPasswordHash(String password_hash) { this.password_hash = password_hash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFullName() { return full_name; }
    public void setFullName(String full_name) { this.full_name = full_name; }

    public boolean isActive() { return is_active; }
    public void setActive(boolean active) { is_active = active; }

    public LocalDateTime getLastLogin() { return last_login; }
    public void setLastLogin(LocalDateTime last_login) { this.last_login = last_login; }

    public LocalDateTime getCreatedAt() { return created_at; }
    public void setCreatedAt(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdatedAt() { return updated_at; }
    public void setUpdatedAt(LocalDateTime updated_at) { this.updated_at = updated_at; }
}
/*
 * CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    last_login DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
 */
