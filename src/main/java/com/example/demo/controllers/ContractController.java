package com.example.demo.controllers;

import com.example.demo.models.Contract;
import com.example.demo.models.Option;
import com.example.demo.models.Tariff;
import com.example.demo.services.ClientService;
import com.example.demo.services.ContractService;
import com.example.demo.services.OptionService;
import com.example.demo.services.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContractController {
    @Autowired
    TariffService tariffService;
    @Autowired
    OptionService optionService;
    @Autowired
    ClientService clientService;
    @Autowired
    ContractService contractService;

    /**
     * This method is used for getting a new number for a new contract
     *
     * @param clientId
     * @param model
     * @return
     */
    @GetMapping(value = "/admin/signContract/{clientId}")
    public String createContract(@PathVariable long clientId, Model model) {
        Contract contract = new Contract();
        contract.setNumber(contractService.getGeneratedNumber());
        model.addAttribute("newContract", contract);
        model.addAttribute("client", clientService.findById(clientId));
        model.addAttribute("tariffs", tariffService.getAll());
        return "/admin/addNewContract";
    }

    /**
     * This method is used to save new contract with connected tariff and client
     *
     * @param clientId
     * @param tariffId
     * @param contract
     * @return
     */
    @PostMapping(value = "/admin/signContract/{clientId}/{tariffId}")
    public String saveCreatedContract(
            @PathVariable long clientId,
            @PathVariable long tariffId,
            @ModelAttribute("newContract") Contract contract,
            Model model) {
        contract.setClient(clientService.findById(clientId));
        contract.setTariff(tariffService.getById(tariffId));
        contractService.saveContract(contract);
        model.addAttribute("contractId", contract.getId());
        return "redirect:/admin/connectOptions/{contractId}";
    }

    /**
     * This method is used to change tariff for existing contract
     *
     * @param contractId
     * @param model
     * @return view with available tariffs
     */
    @PostMapping(value = "/admin/connectTariff/{contractId}")
    public String changeTariff(@PathVariable long contractId, Model model) {
        Tariff tariff = contractService.getContractById(contractId).getTariff();
        model.addAttribute("tariffs", tariffService.getNotAddedToContractTariffs(tariff));
        model.addAttribute("contract", contractService.getContractById(contractId));
        return "/admin/changeTariff";
    }

    /**
     * This method is used by client to change tariff for existing contract
     *
     * @param contractId
     * @param model
     * @return view with available tariffs
     */
    @PostMapping(value = "/profile/connectTariff/{contractId}")
    public String changeTariffByClient(@PathVariable long contractId, Model model) {
        Tariff tariff = contractService.getContractById(contractId).getTariff();
        model.addAttribute("tariffs", tariffService.getNotAddedToContractTariffs(tariff));
        model.addAttribute("contract", contractService.getContractById(contractId));
        return "/client/changeTariff";
    }

    /**
     * This method is used to save selected tariff in contract
     *
     * @param tariffId
     * @return
     */
    @PostMapping(value = "/admin/connectTariff/{contractId}/{tariffId}")
    public String connectTariff(@PathVariable long contractId, @PathVariable long tariffId) {
        Contract contract = contractService.getContractById(contractId);
        Tariff tariff = tariffService.getById(tariffId);
        contractService.connectTariff(contract, tariff);
        return "redirect:/admin/showContractOptions/{contractId}";
    }

    @GetMapping(value = "/admin/connectOptions/{contractId}")
    public String changeOptions(@PathVariable long contractId, Model model) {
        long tariffId = contractService.getContractById(contractId).getTariff().getId();
        model.addAttribute("connectedOptions", optionService.getAllForCertainContract(contractId, tariffId));
        model.addAttribute("availableOptions", optionService.getAllNotAddedToContract(contractId, tariffId));
        model.addAttribute("contract", contractService.getContractById(contractId));
        return "/admin/addOptionsToContract";
    }

    @PostMapping(value = "/admin/connectOption/{contractId}/{optionId}")
    public String connectOptions(@PathVariable long contractId, @PathVariable long optionId) {
        Contract contract = contractService.getContractById(contractId);
        Option option = optionService.getById(optionId);
        contractService.connectOption(contract, option);
        return "redirect:/admin/connectOptions/{contractId}";
    }

    @GetMapping(value = "/profile/connectOptions/{contractId}")
    public String chooseOptionsByClient(@PathVariable long contractId, Model model) {
        long tariffId = contractService.getContractById(contractId).getTariff().getId();
        model.addAttribute("connectedOptions", optionService.getAllForCertainContract(contractId, tariffId));
        model.addAttribute("availableOptions", optionService.getAllNotAddedToContract(contractId, tariffId));
        model.addAttribute("contract", contractService.getContractById(contractId));
        return "/client/addOptionsToContract";
    }

    @PostMapping(value = "/admin/disconnectOption/{contractId}/{optionId}")
    public String disconnectOptions(@PathVariable long contractId, @PathVariable long optionId) {
        Contract contract = contractService.getContractById(contractId);
        Option option = optionService.getById(optionId);
        contractService.disconnectOption(contract, option);
        return "redirect:/admin/showContractOptions/{contractId}";
    }

    @PostMapping(value = "/profile/disconnectOption/{contractId}/{optionId}")
    public String disconnectOptionsByClient(@PathVariable long contractId, @PathVariable long optionId) {
        Contract contract = contractService.getContractById(contractId);
        Option option = optionService.getById(optionId);
        contractService.disconnectOption(contract, option);
        return "redirect:/profile/contractOptions/{contractId}";
    }

    @GetMapping(value = "/admin/showContractOptions/{contractId}")
    public String showContractOptions(@PathVariable long contractId, Model model) {
        Tariff tariff = contractService.getContractById(contractId).getTariff();
        model.addAttribute("options", optionService.getAllForCertainContract(contractId, tariff.getId()));
        model.addAttribute("contract", contractService.getContractById(contractId));
        return "/admin/contractOptions";
    }

    @GetMapping(value = "/profile/contractOptions/{contractId}")
    public String showContractOptionsForClient(@PathVariable long contractId, Model model) {
        Tariff tariff = contractService.getContractById(contractId).getTariff();
        model.addAttribute("options", optionService.getAllForCertainContract(contractId, tariff.getId()));
        model.addAttribute("contract", contractService.getContractById(contractId));
        return "/client/contractOptions";
    }
}
