package org.quarkus.resource;

import org.quarkus.model.User;

public class AuthenticationResponse {
    private String username;
    private String role;
    private String token;

    public AuthenticationResponse(User user, String token) {
        this.username = user.username;
        this.role = user.role;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

