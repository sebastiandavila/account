package com.sofka.account.service;

import com.sofka.account.dto.AccountStatementReport;
import com.sofka.account.dto.CustomerDTO;
import com.sofka.account.entity.Account;
import com.sofka.account.entity.Movement;
import com.sofka.account.repository.AccountRepository;
import com.sofka.account.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class AccountStatementService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @Transactional
    public List<AccountStatementReport> generateAccountStatementReport(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        RestTemplate restTemplate = new RestTemplate();
        String url = customerServiceUrl + "/api/customers/" + customerId;
        ResponseEntity<CustomerDTO> response = restTemplate.getForEntity(url, CustomerDTO.class);
        CustomerDTO customer = new CustomerDTO();
        if (response.getStatusCode().is2xxSuccessful()) {
             customer = response.getBody();
        }

        CustomerDTO finalCustomer = customer;
        return accounts.stream()
                .flatMap(account -> {
                    List<Movement> movements = movementRepository.findByAccountIdAndDateBetween(account.getId(), startDate, endDate);
                    AtomicReference<BigDecimal> initialBalance = new AtomicReference<>(account.getInitialBalance());

                    return movements.stream().map(movement -> {BigDecimal balance = calculateBalance(initialBalance.get(), movement.getAmount());
                        initialBalance.set(balance);
                        return AccountStatementReport.builder()
                                .date(movement.getDate())
                                .customerName(finalCustomer.getName())
                                .accountNumber(account.getAccountNumber())
                                .accountType(account.getAccountType())
                                .initialBalance(account.getInitialBalance())
                                .active(account.getActive())
                                .movementAmount(movement.getAmount())
                                .balance(balance)
                                .build();
                    });
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateBalance(BigDecimal initialBalance, BigDecimal movementAmount) {
        return initialBalance.add(movementAmount);
    }

}
