package ua.training.controller.validator;

import static ua.training.util.ExceptionConstants.EXCEPTION_VALIDATION_INCORRECT_DATE;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import ua.training.exception.ValidationException;

public class DateTimeValidator implements Validator {

    @Override
    public void validate(String dateTime) {
        LocalDateTime inputTime = LocalDateTime.parse(dateTime);
        LocalDateTime nextDay = LocalDateTime.now()
                .plusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        if (!nextDay.isBefore(inputTime) || (inputTime.getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
            throw new ValidationException().setUserMessage(EXCEPTION_VALIDATION_INCORRECT_DATE).addParameter(dateTime);
        }
    }
}
