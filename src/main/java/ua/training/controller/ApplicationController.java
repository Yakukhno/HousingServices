package ua.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.training.controller.validator.DateTimeValidator;
import ua.training.controller.validator.Validator;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.Application;
import ua.training.model.entities.ProblemScale;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.User;
import ua.training.model.service.ApplicationService;
import ua.training.model.service.TypeOfWorkService;
import ua.training.model.UserDetailsImpl;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.List;

import static ua.training.util.AttributeConstants.*;
import static ua.training.util.RouteConstants.NEW_APPLICATION_ROUTE;
import static ua.training.util.RouteConstants.REDIRECT;
import static ua.training.util.RouteConstants.USER_APPLICATIONS_ROUTE;
import static ua.training.util.ViewConstants.ALL_APPLICATIONS_VIEW;
import static ua.training.util.ViewConstants.NEW_APPLICATION_VIEW;
import static ua.training.util.ViewConstants.USER_APPLICATIONS_VIEW;

@Controller
@RequestMapping("/web")
public class ApplicationController {

    private Validator dateTimeValidator = new DateTimeValidator();

    private ApplicationService applicationService;
    private TypeOfWorkService typeOfWorkService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, TypeOfWorkService typeOfWorkService) {
        this.applicationService = applicationService;
        this.typeOfWorkService = typeOfWorkService;
    }

    @GetMapping("/application")
    public String getAllApplication(Model model) {
        model.addAttribute(STATUS_NEW, Application.Status.NEW);
        model.addAttribute(APPLICATIONS, applicationService.getAllApplications());
        return ALL_APPLICATIONS_VIEW;
    }

    @PostMapping("/application")
    public String addApplication(@RequestParam TypeOfWork typeOfWork, @RequestParam ProblemScale problemScale,
                                 @RequestParam LocalDateTime dateTime, @RequestParam String address,
                                 Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Application application = new Application.Builder()
                .setUser(new User.Builder().setId(userDetails.getUser().getId()).build())
                .setTypeOfWork(typeOfWork)
                .setProblemScale(problemScale)
                .setDesiredTime(dateTime)
                .setAddress(address)
                .build();
        applicationService.createNewApplication(application);
        return REDIRECT + USER_APPLICATIONS_ROUTE;
    }

    @GetMapping("/user/application")
    public String getUserApplication(Authentication authentication, Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        model.addAttribute(STATUS_NEW, Application.Status.NEW);
        model.addAttribute(APPLICATIONS, applicationService.getApplicationsByUserId(userDetails.getUser().getId()));
        return USER_APPLICATIONS_VIEW;
    }

    @GetMapping("/new_application")
    public String getNewApplicationPage(Model model) {
        model.addAttribute(TYPE_OF_WORK, typeOfWorkService.getAllTypesOfWork());
        model.addAttribute(PROBLEM_SCALE, ProblemScale.values());
        return NEW_APPLICATION_VIEW;
    }

    @PostMapping("/application/{applicationId}/delete")
    public String deleteApplication(@PathVariable int applicationId, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        applicationService.deleteApplication(applicationId, userDetails.getUser().getId());
        return REDIRECT + USER_APPLICATIONS_ROUTE;
    }

    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationException(ApplicationException e, RedirectAttributes model) {
        if (!e.isUserMessage()) {
            throw e;
        }
        model.addFlashAttribute(MESSAGE, e.getUserMessage());
        List<String> parameters = e.getParameters();
        if (parameters.size() != 0) {
            model.addFlashAttribute(PARAMS, parameters);
        }
        return REDIRECT + NEW_APPLICATION_ROUTE;
    }

    @InitBinder
    public void localDateTimeBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String s) throws IllegalArgumentException {
                LocalDateTime localDateTime = null;
                if (!s.isEmpty()) {
                    dateTimeValidator.validate(s);
                    localDateTime = LocalDateTime.parse(s);
                }
                setValue(localDateTime);
            }
        });
    }
}
