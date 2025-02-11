package com.example.service;

import com.example.repository.AccountRepository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account accountLogin(String username, String password){
        Account account = accountRepository.findAccountByUsernameAndPassword(username, password);
        return account;
    }

    public boolean isExistingUsername(String username) {
        Account account = accountRepository.findAccountByUsername(username);
        return account != null;
    }

    public boolean doesAccountExist(Integer id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        return accountOptional.isPresent();
    }
}
