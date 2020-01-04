package com.nubank.check.balance.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nubank
 */
@Data
@NoArgsConstructor
public class TransactionAccount {

    private double debit;
    private double credit;

    private String id;
    private String acc;

}