package ua.training.controller.validator;

import ua.training.exception.ValidationException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private Pattern patternName
            = Pattern.compile("^[A-zА-яЁёЇїІі]+\\s+[A-zА-яЁёЇїІі]+\\s*$");
    private Pattern patternEmail
            = Pattern.compile("^[\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private Pattern patternPassword
            = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,64}$");

    private static final String EXCEPTION_INCORRECT_NAME
            = "exception.validation.name";
    private static final String EXCEPTION_INCORRECT_EMAIL
            = "exception.validation.email";
    private static final String EXCEPTION_INCORRECT_DATE
            = "exception.validation.date";
    private static final String EXCEPTION_INCORRECT_PASSWORD
            = "exception.validation.password";

    public void validateName(String name) {
        Matcher matcher = patternName.matcher(name);
        if (!matcher.matches()) {
            throw new ValidationException()
                    .setUserMessage(EXCEPTION_INCORRECT_NAME)
                    .addParameter(name);
        }
    }

    public void validateEmail(String email) {
        Matcher matcher = patternEmail.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException()
                    .setUserMessage(EXCEPTION_INCORRECT_EMAIL)
                    .addParameter(email);
        }
    }

    public void validatePassword(String password) {
        Matcher matcher = patternPassword.matcher(password);
        if (!matcher.matches()) {
            throw new ValidationException()
                    .setUserMessage(EXCEPTION_INCORRECT_PASSWORD);
        }
    }

    public void validateDateTime(String dateTime) {
        LocalDateTime inputTime = LocalDateTime.parse(dateTime);
        LocalDateTime nextDay = LocalDateTime.now()
                .plusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        if (!nextDay.isBefore(inputTime)
                || (inputTime.getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
            throw new ValidationException()
                    .setUserMessage(EXCEPTION_INCORRECT_DATE)
                    .addParameter(dateTime);
        }
    }
}
