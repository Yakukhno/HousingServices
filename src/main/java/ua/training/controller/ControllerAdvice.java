package ua.training.controller;

import static ua.training.controller.util.ViewConstants.ERROR_VIEW;
import static ua.training.controller.util.ViewConstants.EXCEPTION_VIEW;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;

import ua.training.exception.AccessForbiddenException;
import ua.training.exception.ResourceNotFoundException;
import ua.training.model.entities.TypeOfWork;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    private Logger logger = Logger.getLogger(ControllerAdvice.class);

    @ExceptionHandler(value = { AccessForbiddenException.class, AccessDeniedException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessForbiddenException() {
        return ERROR_VIEW;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() {
        return ERROR_VIEW;
    }

    @ExceptionHandler(Exception.class)
    public String handleOtherException(Exception e, Model model) {
        logger.error(e.getMessage(), e);
        model.addAttribute("exception", e);
        return EXCEPTION_VIEW;
    }

    @InitBinder
    public void typeOfWorkBinder(WebDataBinder binder) {
        binder.registerCustomEditor(TypeOfWork.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String s) throws IllegalArgumentException {
                TypeOfWork typeOfWork = new TypeOfWork();
                typeOfWork.setId(Integer.parseInt(s));
                setValue(typeOfWork);
            }
        });
    }
}
