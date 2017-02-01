package ua.training.controller.validator;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Validator {

    private static final String NAME_REGEXP
            = "^[A-zА-яЁёЇїІі]+\\s+[A-zА-яЁёЇїІі]+\\s*$";
    private static final String EMAIL_REGEXP = "^[\\w.%+-]+@[A-Za-z0-9.-]" +
            "+\\.[A-Za-z]{2,6}$";
    private static final String PASSWORD_REGEXP
            = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,64}$";

    private static final String EXCEPTION_INCORRECT_NAME
            = "exception.validation.name";
    private static final String EXCEPTION_INCORRECT_EMAIL
            = "exception.validation.email";
    private static final String EXCEPTION_INCORRECT_DATE
            = "exception.validation.date";
    private static final String EXCEPTION_INCORRECT_PASSWORD
            = "exception.validation.password";

    public void validateName(String name) {
        if (!name.matches(NAME_REGEXP)) {
            throw new ValidationException()
                    .setUserMessage(EXCEPTION_INCORRECT_NAME)
                    .addParameter(name);
        }
    }

    public void validateEmail(String email) {
        if (!email.matches(EMAIL_REGEXP)) {
            throw new ValidationException()
                    .setUserMessage(EXCEPTION_INCORRECT_EMAIL)
                    .addParameter(email);
        }
    }

    public void validatePassword(String password) {
        if (!password.matches(PASSWORD_REGEXP)) {
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
