package com.sofka.account.controller;

import com.sofka.account.entity.Account;
import com.sofka.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountController customerController;

    @Test
     void testGetAllAccounts() throws Exception {
        // Arrange
        Account account1 = Account.builder()
                .id(1L)
                .accountNumber("123456")
                .accountType("Savings")
                .initialBalance(BigDecimal.valueOf(1000))
                .active(true)
                .customerId(1L)
                .build();

        Account account2 = Account.builder()
                .id(2L)
                .accountNumber("789012")
                .accountType("Checking")
                .initialBalance(BigDecimal.valueOf(2000))
                .active(true)
                .customerId(2L)
                .build();

        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);
        // Mock the behavior of the account service
        when(accountRepository.findAll()).thenReturn(accounts);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}