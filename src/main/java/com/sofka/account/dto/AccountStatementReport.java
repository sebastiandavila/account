package com.sofka.account.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountStatementReport {

    private LocalDateTime date;
    private String customerName;
    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private boolean active;
    private BigDecimal movementAmount;
    private BigDecimal balance;
}
