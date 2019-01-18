package com.upickem.util;

public class PasswordValidator {

    public static final String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,128}$";

    public static boolean validate(String password) {

        return password.matches(REGEX);
    }
}
