package com.example.demo.services.impl;

import com.example.demo.dao.api.ClientDao;
import com.example.demo.dao.api.ContractDao;
import com.example.demo.dao.api.OptionDao;
import com.example.demo.dao.api.TariffDao;
import com.example.demo.models.entities.Client;
import com.example.demo.models.entities.Contract;
import com.example.demo.models.entities.Option;
import com.example.demo.models.entities.Tariff;
import com.example.demo.services.api.ContractService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Log4j
@Service
@Transactional
public class ContractServiceImpl implements ContractService {
    private final Random random = new Random();
    private static final String BASE_NUMBER = "8920435";

    @Autowired
    ContractDao dao;
    @Autowired
    ClientDao clientDao;
    @Autowired
    TariffDao tariffDao;
    @Autowired
    OptionDao optionDao;

    /**
     * To save contract we have to set client, tariff, price and connection.
     * When we create contract we set tariff price.
     * Connection cost is calculated of connection costs of tariff options.
     * @param contract which has to be saved
     * @param clientId to get client
     * @param tariffId to get tariff
     */
    @Override
    public void saveContract(Contract contract, long clientId, long tariffId) {
        Client client = clientDao.findById(clientId);
        Tariff tariff = tariffDao.getById(tariffId);
        contract.setClient(client);
        contract.setTariff(tariff);
        contract.setPrice(tariff.getPrice());
        contract.setConnectionCost(tariff.getOptions().stream().mapToDouble(Option::getConnectionCost).sum());
        dao.save(contract);
        log.info("Contract is saved to DB");
    }

    @Override
    public Contract getContractById(long id) {
        return dao.getById(id);
    }

    @Override
    public List<Contract> getClientsContracts(long clientId) {
        return dao.getByClientId(clientId);
    }

    /**
     * This method generates number and checks if it is unique.
     * @return unique generated number
     */
    @Override
    public String getGeneratedNumber() {
        String number = generateNumber();
        while (!dao.isNumberUnique(number)) {
            number = generateNumber();
        }
        return number;
    }

    /**
     * Option can't be deleted from the contract if it belongs not to the contract but to the tariff in the contract.
     * If option can be deleted contract's price and connection cost should recalculated.
     * @param contractId
     * @param optionId
     */
    @Override
    public void disconnectOption(long contractId, long optionId) {
        Contract contract = dao.getById(contractId);
        Option option = optionDao.getById(optionId);
        if (contract.delete(option)) {
            double newPrice = contract.getPrice() - option.getPrice();
            contract.setPrice(newPrice);
            double newConnectionCost = contract.getConnectionCost() - option.getConnectionCost();
            contract.setConnectionCost(newConnectionCost);
            dao.update(contract);
            log.info("Option " + option.getName() + " is deleted from the contract");
        } else {
            log.info(option.getName() + " couldn't be deleted from the contract as it belongs to tariff in the contract");
        }
    }

    @Override
    public void blockByAdmin(long contractId) {
        Contract contract = dao.getById(contractId);
        contract.setBlockedByAdmin(true);
        dao.update(contract);
        log.info(contract.getNumber() + " is blocked by admin");
    }

    @Override
    public void unblockByAdmin(long contractId) {
        Contract contract = dao.getById(contractId);
        contract.setBlockedByAdmin(false);
        contract.setBlockedByClient(false);
        dao.update(contract);
        log.info(contract.getNumber() + " is unblocked by admin");
    }

    @Override
    public void blockByClient(long contractId) {
        Contract contract = dao.getById(contractId);
        contract.setBlockedByClient(true);
        dao.update(contract);
        log.info(contract.getNumber() + " is blocked by client");
    }

    @Override
    public void unblockByClient(long contractId) {
        Contract contract = dao.getById(contractId);
        contract.setBlockedByClient(false);
        dao.update(contract);
        log.info("Contract " + contract.getNumber() + " is unblocked by client");
    }

    @Override
    public void terminateContract(long contractId) {
        Contract contract = dao.getById(contractId);
        dao.delete(contract);
        log.info(contract.getNumber() + " is deleted");
    }

    /**
     * There is a constant value BASE_NUMBER and this method generates four last digits for the phone number.
     * As random suffix can contain less than four digits result string is complemented with zeros.
     * @return generated phone number
     */
    private String generateNumber() {
        String result;
        int suffix = random.nextInt(9999) + 1;
        if (suffix < 10)
            result = BASE_NUMBER + "000" + suffix;
        else if (suffix < 100)
            result = BASE_NUMBER + "00" + suffix;
        else if (suffix < 1000)
            result = BASE_NUMBER + "0" + suffix;
        else
            result = BASE_NUMBER + suffix;
        return result;
    }
}
