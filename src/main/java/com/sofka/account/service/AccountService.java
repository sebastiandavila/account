package com.sofka.account.service;

import com.sofka.account.entity.Account;
import com.sofka.account.entity.Movement;
import com.sofka.account.exception.ResourceNotFoundException;
import com.sofka.account.repository.AccountRepository;
import com.sofka.account.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

    @Value("${customer.service.url}")
    private String customerServiceUrl;

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
        try {
            String url = customerServiceUrl + "/api/customers/" + account.getCustomerId();

             restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException e) {

            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("El cliente no existe");
            } else {
                throw new RuntimeException("Error de comunicación con el servicio de clientes", e);
            }
        }
        account.setBalance(account.getInitialBalance());
        return accountRepository.save(account);
    }

    @Transactional
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
            throw new NoSuchElementException("La cuenta no se encontró con el ID especificado: " + id);
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
            throw new NoSuchElementException("La cuenta no se encontró con el ID especificado: " + accountId);
        }
    }
}
