package com.sofka.account.service;

import com.sofka.account.dto.AccountStatementReport;
import com.sofka.account.entity.Account;
import com.sofka.account.entity.Movement;
import com.sofka.account.repository.AccountRepository;
import com.sofka.account.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountStatementService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MovementRepository movementRepository;

    public List<AccountStatementReport> generateAccountStatementReport(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        List<AccountStatementReport> accountStatementReports = new ArrayList<>();

        for (Account account : accounts) {
            List<Movement> movements = movementRepository.findByAccountIdAndDateBetween(account.getId(), startDate, endDate);

            BigDecimal initialBalance = account.getInitialBalance();

            for (Movement movement : movements) {
                BigDecimal balance = calculateBalance(initialBalance, movement.getAmount());
                AccountStatementReport report = AccountStatementReport.builder()
                        .date(movement.getDate())
//                        .customerName(account.getCustomerName())
                        .accountNumber(account.getAccountNumber())
                        .accountType(account.getAccountType())
                        .initialBalance(initialBalance)
                        .active(account.getActive())
                        .movementAmount(movement.getAmount())
                        .balance(balance)
                        .build();
                accountStatementReports.add(report);

                initialBalance = balance;
            }
        }

        return accountStatementReports;
    }

    private BigDecimal calculateBalance(BigDecimal initialBalance, BigDecimal movementAmount) {
        return initialBalance.add(movementAmount);
    }

}
