package ua.training.controller.validator;

import ua.training.exception.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements Validator {

    private static final String EXCEPTION_INCORRECT_PASSWORD
            = "exception.validation.password";
    private Pattern patternPassword = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,64}$"
    );

    @Override
    public void validate(String password) {
        Matcher matcher = patternPassword.matcher(password);
        if (!matcher.matches()) {
            throw new ValidationException()
                    .setUserMessage(EXCEPTION_INCORRECT_PASSWORD);
        }
    }
}
