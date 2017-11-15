package ua.training.controller.validator;

import static ua.training.controller.util.ExceptionConstants.EXCEPTION_VALIDATION_INCORRECT_PASSWORD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.training.exception.ValidationException;

public class PasswordValidator implements Validator {

    private Pattern patternPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,64}$");

    @Override
    public void validate(String password) {
        Matcher matcher = patternPassword.matcher(password);
        if (!matcher.matches()) {
            throw new ValidationException().setUserMessage(EXCEPTION_VALIDATION_INCORRECT_PASSWORD);
        }
    }
}
