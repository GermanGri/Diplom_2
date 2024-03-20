package helper;

import java.security.SecureRandom;

public class Helper {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";

    // Headers endpoints
    public static final String CONTENT_TYPE_LABEL = "Content-type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String BEARER = "Bearer ";

    // Users endpoints
    public static final String AUTH_URL = "/api/auth";
    public static final String USER_REGISTER_URL = "/register";
    public static final String USER_URL = "/user";
    public static final String USER_LOGIN_URL = "/login";

    // Orders endpoints
    public static final String ORDERS_URL = "/api/orders";

    // Ingredients endpoints
    public static final String GET_INGREDIENTS_URL = "/api/ingredients";

    public static final String DICTIONARY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    public static final String DOMAIN = "@yandex.nl"; // :)

    // API Messages
    public static final String SUCCESS_API_RESPONSE = "success";
    public static final String MESSAGE_API_RESPONSE = "message";

    // API Messages description
    public static final String CREDENTIALS_IS_NOT_VALID = "email or password are incorrect";
    public static final String ONE_OF_CRED_IS_MISSING = "Email, password and name are required fields";
    public static final String USER_ALREADY_EXIST = "User already exists";
    public static final String USER_SHOULD_BE_AUTHORIZE = "You should be authorised";
    public static final String SHOULD_BE_PROVIDED = "Ingredient ids must be provided";
    public static final String INCORRECT_VALUE = "One or more ids provided are incorrect";


    // Response codes
    public static final int SUCCESS_RESPONSE_CODE = 200;
    public static final int BAD_REQUEST = 400;
    public static final int ACCESS_DENIED_RESPONSE_CODE = 401;
    public static final int FORBIDDEN_RESPONSE_CODE = 403;

    // Exception messages
    public static final String SOME_ERROR = "Error logging in: ";
    public static final String ERROR_USER = "Error creating user: ";

    public static final String EMPTY_STRING = "";


    public static String generateRandomString(int length) {
        checkArgumentLength(length);

        StringBuilder randomString = new StringBuilder(length);
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(DICTIONARY.length());
            randomString.append(DICTIONARY.charAt(index));
        }

        return randomString.toString();
    }

    public static String generateRandomEmail(int length) {
        checkArgumentLength(length);
        StringBuilder randomEmailName = new StringBuilder();
        SecureRandom random = new SecureRandom();

        randomEmailName.append(DICTIONARY.charAt(random.nextInt(52)));

        for (int i = 1; i < length; i++) {
            randomEmailName.append(DICTIONARY.charAt(random.nextInt(DICTIONARY.length())));
        }

        return String.format("%s%s", randomEmailName, DOMAIN);
    }

    private static void checkArgumentLength(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer");
        }
    }
}
