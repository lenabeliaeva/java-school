package com.example.demo.services;

import com.example.demo.dao.OptionDao;
import com.example.demo.dao.OptionDaoImpl;
import com.example.demo.models.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    OptionDao dao;

    @Override
    @Transactional
    public void add(Option option) {
        dao.add(option);
    }

    @Override
    @Transactional
    public List<Option> getAll() {
        return dao.getAll();
    }

    @Override
    @Transactional
    public List<Option> getAllForCertainTariff(long tariffId) {
        return dao.getAllByTariffId(tariffId);
    }

    @Override
    @Transactional
    public Set<Option> getAllNotAddedToTariff(long tariffId) {
        List<Option> options = dao.getAllNotAddedToTariff(tariffId);
        return new HashSet<>(options);
    }

    @Override
    @Transactional
    public List<Option> getAllForCertainContract(long contractId, long tariffId) {
        List<Option> contractsAndTariffsOptions = new LinkedList<>(dao.getAllByTariffId(tariffId));
        contractsAndTariffsOptions.addAll(dao.getAllByContractId(contractId));
        return contractsAndTariffsOptions;
    }

    @Override
    @Transactional
    public Set<Option> getAllNotAddedToContract(long contractId, long tariffId) {
        List<Option> options = dao.getAllNotAddedToContract(contractId, tariffId);
        options.addAll(dao.getAllNotAddedToTariff(tariffId));
        return new HashSet<>(options);
    }

    @Override
    @Transactional
    public Option getById(long optionId) {
        return dao.getById(optionId);
    }

    @Override
    @Transactional
    public void edit(Option option) {
        dao.update(option);
    }

    @Override
    @Transactional
    public void delete(Option option) {
        dao.delete(option);
    }

}
