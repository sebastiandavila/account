package com.sofka.account.controller;

import com.sofka.account.dto.AccountStatementReport;
import com.sofka.account.entity.Account;
import com.sofka.account.entity.Movement;
import com.sofka.account.service.AccountService;
import com.sofka.account.service.AccountStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ReportController {

    @Autowired
    private AccountStatementService accountService;

    @GetMapping("/reportes")
    public ResponseEntity<List<AccountStatementReport>> generateAccountStatementReport(
            @RequestParam Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate) {
        // Invocar al servicio para generar el reporte
        List<AccountStatementReport> accountStatementReports = accountService.generateAccountStatementReport(customerId, startDate, endDate);
        return ResponseEntity.ok(accountStatementReports);
    }
}
