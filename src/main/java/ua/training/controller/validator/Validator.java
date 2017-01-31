package ua.training.controller.validator;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Validator {

    private static final String NAME_REGEXP = "^[A-zА-яЁёЇї]+ [A-zА-яЁёЇї]+$";
    private static final String EMAIL_REGEXP = "^[\\w.%+-]+@[A-Za-z0-9.-]" +
            "+\\.[A-Za-z]{2,6}$";
    private static final String PASSWORD_REGEXP
            = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,64}$";

    private static final String EXCEPTION_INCORRECT_NAME
            = "Incorrect name %s. Name must contain 2 words";
    private static final String EXCEPTION_INCORRECT_EMAIL
            = "Incorrect email %s";
    private static final String EXCEPTION_INCORRECT_PASSWORD =
            "Incorrect password. Must have at least one digit, " +
                    "one lowercase and uppercase letter, length 8-64 symbols";
    private static final String EXCEPTION_INCORRECT_DATE
            = "Incorrect date %s. You can't choose date, " +
            "which earlier than tomorrow, and Sunday.";

    public void validateName(String name) {
        if (!name.matches(NAME_REGEXP)) {
            String message = String.format(EXCEPTION_INCORRECT_NAME, name);
            throw new ValidationException().setUserMessage(message);
        }
    }

    public void validateEmail(String email) {
        if (!email.matches(EMAIL_REGEXP)) {
            String message = String.format(EXCEPTION_INCORRECT_EMAIL, email);
            throw new ValidationException().setUserMessage(message);
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
            String message = String.format(EXCEPTION_INCORRECT_DATE, dateTime);
            throw new ValidationException().setUserMessage(message);
        }
    }
}
