package ua.training.controller.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.training.model.entities.person.Worker;
import ua.training.model.service.TypeOfWorkService;
import ua.training.model.service.WorkerService;

import static ua.training.controller.Attributes.TYPE_OF_WORK;
import static ua.training.controller.Attributes.WORKERS;
import static ua.training.controller.NewRoutes.REDIRECT;
import static ua.training.controller.NewRoutes.WORKER_ROUTE;
import static ua.training.controller.Views.NEW_WORKER_VIEW;
import static ua.training.controller.Views.WORKERS_VIEW;

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
        return WORKERS_VIEW;
    }

    @PostMapping("/worker")
    public String addWorker(Worker worker) {
        workerService.addNewWorker(worker);
        return REDIRECT + WORKER_ROUTE;
    }

    @GetMapping("/new_worker")
    public String getNewWorkerPage(Model model) {
        model.addAttribute(TYPE_OF_WORK, typeOfWorkService.getAllTypesOfWork());
        return NEW_WORKER_VIEW;
    }
}
