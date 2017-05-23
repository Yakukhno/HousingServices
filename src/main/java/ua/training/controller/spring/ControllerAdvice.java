package ua.training.controller.spring;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import ua.training.exception.AccessForbiddenException;
import ua.training.exception.ResourceNotFoundException;
import ua.training.model.entities.TypeOfWork;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

import static ua.training.controller.Attributes.MESSAGE;
import static ua.training.controller.Views.ERROR_VIEW;
import static ua.training.controller.Views.EXCEPTION_VIEW;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    private Logger logger = Logger.getLogger(ControllerAdvice.class);

    // TODO: 06.05.2017 send 403 status code
    @ExceptionHandler(AccessForbiddenException.class)
    public String handleAccessForbiddenException(AccessForbiddenException e,
                                                 Model model)
            throws IOException {
        model.addAttribute(MESSAGE, e.getMessage());
        return ERROR_VIEW;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(ResourceNotFoundException e,
                                                  Model model)
            throws IOException {
        model.addAttribute(MESSAGE, e.getMessage());
        return ERROR_VIEW;
    }

//    @ExceptionHandler(Exception.class)
//    public String handleOtherException(Exception e, Model model)
//            throws IOException {
//        logger.error(e.getMessage(), e);
//        model.addAttribute("exception", e);
//        return EXCEPTION_VIEW;
//    }

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
