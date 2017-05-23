package ua.training.controller.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.training.model.service.BrigadeService;
import ua.training.model.service.impl.BrigadeServiceImpl;

import static ua.training.controller.Attributes.BRIGADE;
import static ua.training.controller.Views.BRIGADE_VIEW;

@Controller
@RequestMapping("/rest/brigade")
public class BrigadeController {

    private BrigadeService brigadeService;

    @Autowired
    public BrigadeController(BrigadeService brigadeService) {
        this.brigadeService = brigadeService;
    }

    @GetMapping("/{id}")
    public String getTasks(@PathVariable int id, Model model) {
        model.addAttribute(BRIGADE, brigadeService.getBrigadeById(id));
        return BRIGADE_VIEW;
    }
}
