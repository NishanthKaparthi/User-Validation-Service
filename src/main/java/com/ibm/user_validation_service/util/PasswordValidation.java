package com.ibm.user_validation_service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PasswordValidation class validates the input password based on the required conditions.
 * Password should be greater than 8 characters, contains at least a number, a capital letter, a special character
 */
@Component
@Slf4j
public class PasswordValidation {

    public static boolean validatePassword(String password) {
        return password.length() > 8 &&
                containsNumber(password) &&
                containsCapitalLetter(password) &&
                containsSpecialCharacter(password);
    }

    private static boolean containsNumber(String password) {
        return password.matches(".*\\d.*");
    }

    private static boolean containsCapitalLetter(String password) {
        return password.matches(".*[A-Z].*");
    }

    /**
     * for validating specialChar, created a Pattern obj with given chars,
     * creating a matcher object by validating the pwd against the pattern
     * if the pwd contains at lease one of the char, return true, else false
     */
    private static boolean containsSpecialCharacter(String password) {
        Pattern specialCharPattern = Pattern.compile("[_#$%.]");
        Matcher matcher = specialCharPattern.matcher(password);
        return matcher.find();
    }
}
