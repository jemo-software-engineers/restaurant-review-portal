package com.jemo.RestaurantReviewPortal.auth;

public class LoginResponse {
    private String jwt;

    public LoginResponse(String jwt) {
        this.jwt = jwt;
    }

    // Getter

    public String getJwt() {
        return jwt;
    }
}
