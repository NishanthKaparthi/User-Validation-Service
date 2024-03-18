package com.ibm.user_validation_service.util;

/**
 * ValidationConstants class holds all the constants used within the application.
 */
public class ValidationConstants {
    public static final String NULL_API_RESPONSE = "Received null response from api";
    public static final String NOT_ELIGIBLE_TO_REGISTER = "You are not eligible to register as you are not from Canada (or) input IP might not be appropriate";
    public static final String INVALID_PASSWORD = "Password validation failed, it should be greater than 8 characters and" +
            " should contain a number, a capital letter and a special character in \"_ # $ % .\" ";
    public static final String BLANK_PARAMS = "Parameters should not be blank/null, please input username, password and IP address";
    public static final String INCOMING_REQUEST = "Incoming user request: {}";
    public static final String OUTGOING_RESPONSE = "Outgoing user response: {}";
    public static final String NON_BLANK_PARAMS = "All the parameters are non null/non-blank";
    public static final String VALID_PASSWORD = "Input password meets the standard";
    public static final String MAKE_API_CALL = "Making api call using URL: {}";
    public static final String VALIDATE_PWD = "Validating the password to ensure the required conditions are met";
    public static final String VALIDATE_PARAMS = "Validating the fields to ensure they are not empty.";
    public static final String SEPARATOR = "--------------------------------------------------------------------------";
    public static final String BAD_REQUEST_LOG = NOT_ELIGIBLE_TO_REGISTER + BLANK_PARAMS + INVALID_PASSWORD;
    public static final String DATA_NOT_FOUND = "No Data Found from API";
    public static final String COUNTRY = "Canada";
    public static final String ERROR_TEXT = "Encountered an exception while processing: {}";
}
