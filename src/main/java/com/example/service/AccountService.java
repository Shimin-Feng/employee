package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = new Account();
        account.setUsername(username);
        Example<Account> example = Example.of(account);
        Optional<Account> optional = accountRepository.findOne(example);
        return optional.map(value -> User.withUsername(value.getUsername()).password(value.getPassword()).roles(value.getRole()).build()).orElse(null);
    }
}