package ua.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.training.model.service.BrigadeService;

import static ua.training.controller.util.Attributes.BRIGADE;
import static ua.training.controller.util.Views.BRIGADE_VIEW;

@Controller
@RequestMapping("/web/brigade")
public class BrigadeController {

    private BrigadeService brigadeService;

    @Autowired
    public BrigadeController(BrigadeService brigadeService) {
        this.brigadeService = brigadeService;
    }

    @GetMapping("/{id}")
    public String getBrigade(@PathVariable int id, Model model) {
        model.addAttribute(BRIGADE, brigadeService.getBrigadeById(id));
        return BRIGADE_VIEW;
    }
}
