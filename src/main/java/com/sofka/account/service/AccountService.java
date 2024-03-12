package com.sofka.account.service;

import com.sofka.account.entity.Account;
import com.sofka.account.entity.Movement;
import com.sofka.account.repository.AccountRepository;
import com.sofka.account.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MovementRepository movementRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElse(null);
    }

    public List<Account> getAccountsByCustomerId(Long id) {
        return accountRepository.findByCustomerId(id);
    }

    public Account createAccount(Account account) {
        account.setBalance(account.getInitialBalance());
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account existingAccount = optionalAccount.get();

            existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
            existingAccount.setAccountType(updatedAccount.getAccountType());
            existingAccount.setInitialBalance(updatedAccount.getInitialBalance());
            existingAccount.setActive(updatedAccount.getActive());
            return accountRepository.save(existingAccount);
        } else {
             throw new NoSuchElementException("Account not found with id: " + id);
        }
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public void registerMovement(Long accountId, Movement movement) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            BigDecimal newBalance = account.getInitialBalance().add(movement.getAmount());
            account.setInitialBalance(newBalance);
            accountRepository.save(account);

            movement.setBalance(newBalance);
            movementRepository.save(movement);
        } else {
            throw new NoSuchElementException("Account not found with id: " + accountId);
        }
    }
}
