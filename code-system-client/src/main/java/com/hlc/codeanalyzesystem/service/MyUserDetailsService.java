package com.hlc.codeanalyzesystem.service;

import com.hlc.codeanalyzesystem.dao.ClientDao;
import com.hlc.codeanalyzesystem.entities.Client;
import com.hlc.codeanalyzesystem.entity.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    private ClientDao clientDao;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientDao.selectByUsername(username);
        if(client == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        List<GrantedAuthority> auths =
                AuthorityUtils.createAuthorityList("ROLE_scut");
        return new SystemUser(client.getId(),client.getUsername(),passwordEncoder.encode(client.getPassword()),auths);
    }
}
