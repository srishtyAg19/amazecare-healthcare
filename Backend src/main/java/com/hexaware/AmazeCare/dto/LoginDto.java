package com.hexaware.AmazeCare.dto;

public class LoginDto {
    private String username; // Updated to lowercase for naming conventions
    private String password;
    private String email;

    // Default constructor
    public LoginDto() { }

    // Parameterized constructor
    public LoginDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email=email;
    }

    // Getters and Setters
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
    public String getEmail() {
        return email;
    }

    public void setEmail(String password) {
        this.email = email;
    }
    

    // Override toString for debugging
    @Override
    public String toString() {
        return "LoginDto [username=" + username + ", password=" + password + "]";
    }
}
