package com.example.demo.services;

import com.example.demo.dao.OptionDao;
import com.example.demo.dao.OptionDaoImpl;
import com.example.demo.models.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<Option> getAllNotAddedToTariff(long tariffId) {
        return dao.getAllNotAddedToTariff(tariffId);
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
