package com.example.food_product_comparison_android_app;

import java.util.*;
import java.util.regex.Pattern;

public class Utils {
    // This class is for general, common data that is being shared among multiple units
    public static final int CAMERA_REQUEST_CODE = 814736521;
    public static final int NUTRITION_TABLE_PIC_REQUEST = 57219432;
    public static final int PRODUCT_LOOK_PIC_REQUEST = 307188697;
    public static final int RC_SIGN_IN = 900914;
    public static final int NOT_LOGGED_IN = 0;
    public static final int LOCAL_LOGIN = 1;
    public static final int FACEBOOK_LOGIN = 2;
    public static final int GOOGLE_LOGIN = 3;
    public static final int MAX_LEN_USERNAME = 10;
    public static final int MAX_LEN_FIRSTNAME = 25;
    public static final int MAX_LEN_LASTNAME = 25;
    public static final int MAX_LEN_EMAIL = 45;
    public static final int MAX_LEN_PASSWORD = 20;
    public static final String USER_INFO_KEY = "USER_TRANSIT_KEY";

    // User Input Validation
    public static final String REGEX_CONTAIN_LOWERCASE = "^.*[a-z].*$";
    public static final String REGEX_CONTAIN_UPPERCASE = "^.*[A-Z].*$";
    public static final int EMAIL_INPUT = 0;
    public static final int USERNAME_INPUT = 1;
    public static final int PASSWORD_INPUT = 2;
    public static final int FLNAME_INPUT = 3;
    public static final String EMPTY_INPUT = "This field cannot be empty.";
    public static final String EMAIL_INPUT_ERROR = "Invalid email address format, please check again.";
    public static final String USERNAME_INPUT_ERROR = "Username must not contain any special characters (e.g., !@#$%^&*).";
    public static final String PASSWORD_INPUT_TOO_SHORT_ERROR = "Password must contain at least 8 characters.";
    public static final String PASSWORD_INPUT_NO_LOWERCASE_ERROR = "Password must contain at least one lowercase character.";
    public static final String PASSWORD_INPUT_NO_UPPERCASE_ERROR = "Password must contain at least one uppercase character.";
    public static final String FLNAME_INPUT_ERROR = "This field must only contain English characters.";
    public static final String CONFIRM_PASSWORD_NOT_MATCH_ERROR = "Passwords do not match.";
    public static final String USER_INPUT_VALID = "SUCCESS";

    // Animation
    public static final int login_view_animation_translation = 300;
    public static final int login_view_animation_duration = 600;


    public static String validateUserInput(String input, int input_type)
    {
        if (input.isEmpty())
            return EMPTY_INPUT;

        String regexPattern;

        switch (input_type)
        {
            case EMAIL_INPUT:
                regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_.-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
                if (!patternMatches(input, regexPattern))
                    return EMAIL_INPUT_ERROR;
                else
                    break;
            case USERNAME_INPUT:
                regexPattern = "^[a-zA-Z0-9]{1,10}$";
                if (!patternMatches(input, regexPattern))
                    return USERNAME_INPUT_ERROR;
                else
                    break;
            case PASSWORD_INPUT:
                regexPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
                if (!patternMatches(input, regexPattern))
                {
                    if (input.length() < 8) {
                        return PASSWORD_INPUT_TOO_SHORT_ERROR;
                    } else if (!patternMatches(input, REGEX_CONTAIN_LOWERCASE))
                    {
                        return PASSWORD_INPUT_NO_LOWERCASE_ERROR;
                    }
                    else
                    {
                        return PASSWORD_INPUT_NO_UPPERCASE_ERROR;
                    }
                }
                else
                    break;
            case FLNAME_INPUT:
                regexPattern = "^[a-zA-Z]*$";
                if (!patternMatches(input, regexPattern))
                    return FLNAME_INPUT_ERROR;
                else
                    break;
            default:
                break;
        }

        return USER_INPUT_VALID;
    }

    public static boolean patternMatches(String input, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(input)
                .matches();
    }
}
