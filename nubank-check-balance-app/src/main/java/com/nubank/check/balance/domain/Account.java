package com.nubank.check.balance.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {

    private String accountId;
    private String customerId;

    private double balance;

    public static Account unknownAccount() {
        return new Account("-1", "-1", 0D);
    }
}
