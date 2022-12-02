package com.CUBank.creditunionbank.utils;

public class LoggedInContext {

    private static final ThreadLocal<String> CURRENT_USER = new ThreadLocal<>();

    public static void setCurrentUser(final String currentUser) {
        CURRENT_USER.set(currentUser);
    }

    public static String getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}
