package com.sofka.account.service;

import com.sofka.account.entity.Account;
import com.sofka.account.repository.AccountRepository;
import org.junit.Assert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

   @Mock
   private RestTemplate restTemplate;

    @Test
     void testGetAccountById() {
        Long accountId = 1L;
        Account account =  Account.builder()
                .id(accountId)
                .accountNumber("123456")
                .accountType("Savings")
                .initialBalance(BigDecimal.valueOf(1000))
                .active(true)
                .customerId(1L)
                .build();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountById(accountId);
        Assert.assertNotNull(result);
        Assert.assertEquals(accountId, result.getId());
    }

    @Test
     void testCreateAccount() {
        Account account =  Account.builder()
                .accountNumber("789012")
                .accountType("Checking")
                .initialBalance(BigDecimal.valueOf(2000))
                .active(true)
                .customerId(2L)
                .build();
        when(accountRepository.save(account)).thenReturn(account);
       when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(new ResponseEntity<>("{\"id\": 1, \"name\": \"Sebastian\"}", HttpStatus.OK));

        Account result = accountService.createAccount(account);
        Assert.assertNotNull(result);
        Assert.assertEquals("789012", result.getAccountNumber());
    }
}

