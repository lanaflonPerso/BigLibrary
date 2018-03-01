package ua.khai.slynko.library.constant;

public final class Constants {
    private Constants() {
    }

    public static final String TRUE = "true";
    public static final int STRING_MAX_LENGTH = 50;
    public static final int STRING_MIN_LENGTH = 1;
    public static final int PASSWORD_MAX_LENGTH = 20;
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int EMAIL_MAX_LENGTH = 30;
    public static final int EMAIL_MIN_LENGTH = 5;
    public static final int LOGIN_MAX_LENGTH = 20;
    public static final int LOGIN_MIN_LENGTH = 3;

    public static final String EMAIL_PATTERN = "^[a-zA-Z][a-zA-Z0-9\\.-]*\\@[a-zA-Z]+\\.[a-zA-Z]+$";
    public static final String DATE_PATTERN = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
}
