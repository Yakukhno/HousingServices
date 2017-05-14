package ua.training.controller.spring;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.util.List;

import static ua.training.controller.Attributes.*;

@Controller
@RequestMapping("/rest")
public class ApplicationController {

    private final String ALL_APPLICATIONS_VIEW = "application/applications";
    private final String NEW_APPLICATION_VIEW = "application/new_application";
    private final String NEW_APPLICATION_REDIRECT
            = "redirect:/rest/new_application";
    private static final String USER_APPLICATIONS_VIEW
            = "application/tenant_applications";
    private static final String USER_APPLICATIONS_REDIRECT
            = "redirect:/rest/user/application";

    private Validator dateTimeValidator = new DateTimeValidator();

    private ApplicationService applicationService;
    private TypeOfWorkService typeOfWorkService;

    @Autowired
    public ApplicationController(ApplicationService applicationService,
                                 TypeOfWorkService typeOfWorkService) {
        this.applicationService = applicationService;
        this.typeOfWorkService = typeOfWorkService;
    }

    @GetMapping("/application")
    public String getAllApplication(@SessionAttribute User user, Model model) {
        model.addAttribute(STATUS_NEW, Application.Status.NEW);
        model.addAttribute(APPLICATIONS,
                applicationService.getAllApplications(user.getRole()));
        return ALL_APPLICATIONS_VIEW;
    }

    @PostMapping("/application")
    public String addApplication(@RequestParam TypeOfWork typeOfWork,
                                 @RequestParam ProblemScale problemScale,
                                 @RequestParam LocalDateTime dateTime,
                                 @RequestParam String address,
                                 @SessionAttribute("user") User sessionUser,
                                 Model model) {
        Application application = new Application.Builder()
                .setTenant(new User.Builder()
                        .setId(sessionUser.getId())
                        .build())
                .setTypeOfWork(typeOfWork)
                .setProblemScale(problemScale)
                .setDesiredTime(dateTime)
                .setAddress(address)
                .build();
        applicationService.createNewApplication(application);
        return USER_APPLICATIONS_REDIRECT;
    }

    @GetMapping("/user/application")
    public String getUserApplication(@SessionAttribute User user, Model model) {
        model.addAttribute(STATUS_NEW, Application.Status.NEW);
        model.addAttribute(APPLICATIONS,
                applicationService.getApplicationsByUserId(user.getId()));
        return USER_APPLICATIONS_VIEW;
    }

    @GetMapping("/new_application")
    public String getNewApplicationPage(Model model) {
        model.addAttribute(TYPE_OF_WORK, typeOfWorkService.getAllTypesOfWork());
        model.addAttribute(PROBLEM_SCALE, ProblemScale.values());
        return NEW_APPLICATION_VIEW;
    }

    @PostMapping("/application/{applicationId}/delete")
    public String deleteApplication(@PathVariable int applicationId,
                                    @SessionAttribute User user) {
        applicationService.deleteApplication(applicationId, user.getId());
        return USER_APPLICATIONS_REDIRECT;
    }

    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationException(ApplicationException e,
                                             RedirectAttributes model) {
        if (e.isUserMessage()) {
            model.addFlashAttribute(MESSAGE, e.getUserMessage());
            List<String> parameters = e.getParameters();
            if (parameters.size() != 0) {
                model.addFlashAttribute(PARAMS, parameters);
            }
            return NEW_APPLICATION_REDIRECT;
        } else {
            throw e;
        }
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
