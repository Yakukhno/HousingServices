package ua.training.controller.validator;

import ua.training.exception.ValidationException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DateTimeValidator implements Validator {

    private static final String EXCEPTION_INCORRECT_DATE
            = "exception.validation.date";

    @Override
    public void validate(String dateTime) {
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
