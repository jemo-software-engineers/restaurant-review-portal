package com.jemo.RestaurantReviewPortal.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String jwt;
    private String role;

    public LoginResponse(String jwt, String role) {
        this.jwt = jwt;
        this.role = role;
    }
}
