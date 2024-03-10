package helper;

import java.util.Random;

public class Helper {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String USER_REGISTER_URL = "/api/auth/register";
    public static final String USER_URL = "/api/auth/user";
    public static final String USER_LOGIN_URL = "/api/auth/login";
    public static final String ORDERS_URL = "/api/orders";
    public static final String GET_INGREDIENTS_URL = "/api/ingredients";
    public static final String CREATE_ORDERS = "/api/orders";

    public static final String DICTIONARY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static final String CONTENT_TYPE_LABEL = "Content-type";
    public static final String CONTENT_TYPE_VALUE = "application/json";




    public static String generateRandomString(int length) {
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(DICTIONARY.length());
            randomString.append(DICTIONARY.charAt(index));
        }
        return randomString.toString();
    }

    public static String generateRandomEmail(int length) {
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(DICTIONARY.length());
            randomString.append(DICTIONARY.charAt(index));
        }

        String domain = "@example.com";

        return randomString + domain;
    }

}
