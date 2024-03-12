package com.sofka.account.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String gender;
    private int age;
    private String identification;
    private String address;
    private String phoneNumber;
    private Long clientId;
    private String password;
    private boolean status;
}
