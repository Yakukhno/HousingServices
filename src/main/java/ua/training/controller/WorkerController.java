package ua.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.training.controller.validator.NameValidator;
import ua.training.controller.validator.Validator;
import ua.training.exception.ApplicationException;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.TypeOfWorkService;
import ua.training.model.service.WorkerService;

import java.util.List;

import static ua.training.controller.util.Attributes.*;
import static ua.training.controller.util.Routes.*;
import static ua.training.controller.util.Views.NEW_WORKER_VIEW;
import static ua.training.controller.util.Views.WORKERS_VIEW;

@Controller
@RequestMapping("/web")
public class WorkerController {

    private Validator nameValidator = new NameValidator();

    private WorkerService workerService;
    private TypeOfWorkService typeOfWorkService;

    @Autowired
    public WorkerController(WorkerService workerService,
                            TypeOfWorkService typeOfWorkService) {
        this.workerService = workerService;
        this.typeOfWorkService = typeOfWorkService;
    }

    @GetMapping("/worker")
    public String getAllWorkers(Model model) {
        model.addAttribute(WORKERS, workerService.getAllWorkers());
        return WORKERS_VIEW;
    }

    @PostMapping("/worker")
    public String addWorker(Worker worker) {
        nameValidator.validate(worker.getName());
        workerService.addNewWorker(worker);
        return REDIRECT + WORKER_ROUTE;
    }

    @GetMapping("/new_worker")
    public String getNewWorkerPage(Model model) {
        model.addAttribute(TYPE_OF_WORK, typeOfWorkService.getAllTypesOfWork());
        return NEW_WORKER_VIEW;
    }

    @ExceptionHandler(ApplicationException.class)
    public String handleApplicationException(ApplicationException e,
                                             RedirectAttributes model) {
        if (!e.isUserMessage()) {
            throw e;
        }
        model.addFlashAttribute(MESSAGE, e.getUserMessage());
        List<String> parameters = e.getParameters();
        if (parameters.size() != 0) {
            model.addFlashAttribute(PARAMS, parameters);
        }
        return REDIRECT + NEW_WORKER_ROUTE;
    }
}
