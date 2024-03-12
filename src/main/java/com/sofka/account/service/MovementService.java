package com.sofka.account.service;

import com.sofka.account.entity.Account;
import com.sofka.account.entity.Movement;
import com.sofka.account.exception.InsufficientBalanceException;
import com.sofka.account.repository.MovementRepository;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovementService {

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private AccountService accountService;


    private final AsyncHttpClient asyncHttpClient;

    public MovementService() {
        asyncHttpClient = new DefaultAsyncHttpClient();
    }

    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }

    public Movement getMovementById(Long id) {
        return movementRepository.findById(id)
                .orElse(null);
    }

    public Movement createMovement(Movement movement) {

        Account account = accountService.getAccountById(movement.getAccountId());

        if (account.getBalance().add(movement.getAmount()).compareTo(BigDecimal.ZERO) >= 0) {
            movement.setBalance(account.getBalance());
            Movement createdMovement = movementRepository.save(movement);
            account.setBalance(account.getBalance().add(movement.getAmount()));
            accountService.updateAccount(movement.getAccountId(), account);
            return createdMovement;
        } else {
            throw new InsufficientBalanceException("Saldo insuficiente para realizar este movimiento");
        }



//        String url = "http://cliente-persona-service/api/updateClientInformation";
//        Request request = asyncHttpClient.preparePost(url)
//                .setBody("accountId=" + accountId + "&movementId=" + createdMovement.getId())
//                .build();
//
//        asyncHttpClient.executeRequest(request)
//                .toCompletableFuture()
//                .thenAccept(response -> {
//                    System.out.println("Notification sent successfully: " + response.getStatusCode());
//                })
//                .exceptionally(throwable -> {
//                    System.err.println("Error sending notification: " + throwable.getMessage());
//                    return null;
//                });

//        return createdMovement;
    }

    public void deleteMovement(Long id) {
        movementRepository.deleteById(id);
    }
}