package org.cshaifasweng.winter.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class SecurityConstants {
    public static final String AUTH_LOGIN_URL = "/login";
    public static final String AUTH_LOGOUT_URL = "/logout";
    public static String JWT_SECRET = "ReplaceThis";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "Lilach-API";
    public static final String TOKEN_AUDIENCE = "Lilach-Client";
    public static final String PRIVILEGE_CATALOG_EDIT = "CATALOG_EDIT_PRIVILEGE";
    public static final String PRIVILEGE_USERS_EDIT = "USERS_EDIT_PRIVILEGE";
    public static final String PRIVILEGE_COMPLAINT_FILE = "COMPLAINT_FILE_PRIVILEGE";
    public static final String PRIVILEGE_COMPLAINT_HANDLE = "COMPLAINT_HANDLE_PRIVILEGE";
    public static final String PRIVILEGE_ROLES_EDIT = "ROLES_EDIT_PRIVILEGE";
    public static final String PRIVILEGE_ORDERS_CREATE = "ORDERS_CREATE_PRIVILEGE";
    public static final String PRIVILEGE_ORDERS_CANCEL = "ORDERS_CANCEL_PRIVILEGE";

    @Autowired
    public SecurityConstants(@Value("${security.jwt-secret}") String secret) {
        JWT_SECRET = secret;
    }
}
