package ua.training.controller;

import static ua.training.util.AttributeConstants.BRIGADE;
import static ua.training.util.ViewConstants.BRIGADE_VIEW;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.training.model.service.BrigadeService;

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
