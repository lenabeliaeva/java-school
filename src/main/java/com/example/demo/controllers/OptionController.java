package com.example.demo.controllers;

import com.example.demo.models.Option;
import com.example.demo.services.OptionService;
import com.example.demo.services.OptionServiceImpl;
import com.example.demo.services.TariffService;
import com.example.demo.services.TariffServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class OptionController {

    TariffService tariffService;
    OptionService optionService;

    public OptionController(TariffService tariffService, OptionService optionService) {
        this.tariffService = tariffService;
        this.optionService = optionService;
    }

    @GetMapping(value = "/options")
    public String showAllOptions(Model model) {
        model.addAttribute("options", optionService.getAll());
        return "options";
    }

    @GetMapping(value = "/showOptions/{tariffId}")
    public String showOptionsForTariff(@PathVariable String tariffId, Model model) {
        List<?> options = optionService.getAllForCertainTariff(Long.parseLong(tariffId));
        model.addAttribute("options", options);
        model.addAttribute("tariff", tariffService.getById(Long.parseLong(tariffId)));
        return "tariffOptions";
    }

    @PostMapping(value = "/createOption")
    public String createOption(Model model) {
        model.addAttribute("newOption", new Option());
        return "addNewOption";
    }

    @PostMapping(value = "/saveOption")
    public String saveOption(@Valid @ModelAttribute("newOption") Option option, BindingResult result) {
        if (result.hasErrors()) {
            return "addNewOption";
        }
        optionService.add(option);
        log.info("New option is saved to DB");
        return "redirect:/options";
    }

    @PostMapping(value = "/editOption/{optionId}")
    public String editOption(@PathVariable String optionId, Model model) {
        model.addAttribute("editedOption", optionService.getById(Long.parseLong(optionId)));
        return "editOption";
    }

    @PostMapping(value = "/saveEditedOption")
    public String saveEditedOption(@ModelAttribute("editedOption") Option edited) {
        Option initial = optionService.getById(edited.getId());
        if (edited.getTariff() == null)
            edited.setTariff(initial.getTariff());
        optionService.edit(edited);
        return "redirect:/options";
    }

    @PostMapping("/deleteOption/{optionId}")
    public String deleteOption(@PathVariable String optionId) {
        optionService.delete(optionService.getById(Long.parseLong(optionId)));
        return "redirect:/options";
    }
}
