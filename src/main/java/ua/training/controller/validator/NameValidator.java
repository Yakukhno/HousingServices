package ua.training.controller.validator;

import static ua.training.controller.util.ExceptionConstants.EXCEPTION_VALIDATION_INCORRECT_NAME;

import ua.training.exception.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator implements Validator {

    private Pattern patternName = Pattern.compile("^[A-zА-яЁё'ЇїІі]+\\s+[A-zА-яЁё'ЇїІі]+\\s*$");

    @Override
    public void validate(String name) {
        Matcher matcher = patternName.matcher(name);
        if (!matcher.matches()) {
            throw new ValidationException().setUserMessage(EXCEPTION_VALIDATION_INCORRECT_NAME).addParameter(name);
        }
    }
}
