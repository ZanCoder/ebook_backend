package com.ebook.security;

public class Endpoints {

    public static final String FRONT_END_HOST = "http://localhost:3000";

    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "/products",
            "/products/**",
            "/brands",
            "/brands/**",
            "/image-product",
            "/image-product/**",
            "/users/search/existsByUsername",
            "/users/search/existsByEmail",
            "/api/user/active",
    };

    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/user/register",
            "/api/user/login",
    };

    public static final String[] ADMIN_GET_ENDPOINTS = {
            "/users",
            "/users/**",
    };

    public static final String[] ADMIN_POST_ENDPOINTS = {
            "/products",
            "/brands"
    };

    public static final String[] ADMIN_DELETE_ENDPOINTS = {
            "/products",
            "/products/**",
            "/brands"
    };
}
