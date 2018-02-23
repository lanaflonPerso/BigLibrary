package ua.khai.slynko.Library.constant;

import java.util.regex.Pattern;

public final class Constants {
    private Constants() {
    }

    public static final Pattern PATTERN_NUMBER = Pattern.compile("^[0-9]+$");
}
