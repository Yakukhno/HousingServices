package ua.training.controller.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ua.training.model.entities.TypeOfWork;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.TypeOfWorkService;
import ua.training.model.service.WorkerService;

import java.beans.PropertyEditorSupport;

import static ua.training.controller.Attributes.TYPE_OF_WORK;
import static ua.training.controller.Attributes.WORKERS;

@Controller
@RequestMapping("/rest")
public class WorkerController {

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
        return "worker/workers";
    }

    @PostMapping("/worker")
    public String addWorker(Worker worker) {
        workerService.addNewWorker(worker);
        return "redirect:/rest/worker";
    }

    @GetMapping("/new_worker")
    public String getNewWorkerPage(Model model) {
        model.addAttribute(TYPE_OF_WORK, typeOfWorkService.getAllTypesOfWork());
        return "worker/new_worker";
    }
}
