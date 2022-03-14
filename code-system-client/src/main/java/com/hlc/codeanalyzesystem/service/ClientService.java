package com.hlc.codeanalyzesystem.service;

import com.hlc.codeanalyzesystem.dao.ClientDao;
import com.hlc.codeanalyzesystem.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientDao clientDao;

    public Client selectByUsername(String username){
        return clientDao.selectByUsername(username);
    }

    public Client selectById(Integer id){
        return clientDao.selectByPrimaryKey(id);
    }
}
