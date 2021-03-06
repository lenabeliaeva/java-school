package com.example.demo.controllers;

import com.example.demo.exceptions.UserAlreadyExistsException;
import com.example.demo.models.entities.Client;
import com.example.demo.services.api.ClientService;
import com.example.demo.services.api.ContractService;
import com.example.demo.services.api.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class ClientController {

    @Autowired
    ClientService clientService;
    @Autowired
    ContractService contractService;
    @Autowired
    SecurityService securityService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("client", new Client());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerClient(@ModelAttribute("client") @Valid Client client, BindingResult br) {
        if (br.hasErrors()) {
            return "registration";
        }
        try {
            clientService.registerNewClient(client);
            securityService.autologin(client.getEmail(), client.getPassword());
        } catch (UserAlreadyExistsException e) {
            return "registration";
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String showWelcomePage(Model model) {
        Client client = clientService.getAuthorizedClient();
        if (client != null) {
            model.addAttribute("client", client);
            model.addAttribute("contracts", contractService.getClientsContracts(client.getId()));
            return "profile";
        } else {
            return "welcome";
        }
    }

    @PostMapping("/editProfile")
    public String saveEditedProfile(@ModelAttribute @Valid Client client, BindingResult br) {
        if (br.hasErrors()) {
            return "profile";
        }
        clientService.editClientProfile(client);
        return "redirect:/";
    }

    @PostMapping("/profile/blockContract/{contractId}")
    public String blockContract(@PathVariable long contractId) {
        contractService.blockByClient(contractId);
        return "redirect:/";
    }

    @PostMapping("/profile/unblockContract/{contractId}")
    public String unblockContract(@PathVariable long contractId) {
        contractService.unblockByClient(contractId);
        return "redirect:/";
    }
}
