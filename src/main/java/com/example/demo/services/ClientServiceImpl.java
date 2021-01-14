package com.example.demo.services;

import com.example.demo.dao.ClientDao;
import com.example.demo.exceptions.UserAlreadyExistsException;
import com.example.demo.models.Client;
import com.example.demo.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private ClientDao dao;

    @Autowired
    public void setDao(ClientDao dao) {
        this.dao = dao;
    }

    private BCryptPasswordEncoder encoder;

    @Autowired
    public void setEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void registerNewClient(Client client) throws UserAlreadyExistsException {
        if (dao.findByEmail(client.getEmail()) != null) {
            throw new UserAlreadyExistsException("There is an account with this email: " + client.getEmail());
        }
        client.setPassword(encoder.encode(client.getPassword()));
        client.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        dao.register(client);
    }

    @Override
    public Client findByEmail(String email) {
        return dao.findByEmail(email);
    }
}
