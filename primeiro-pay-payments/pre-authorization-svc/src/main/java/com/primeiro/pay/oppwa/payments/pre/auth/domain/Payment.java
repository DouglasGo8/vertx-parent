package com.primeiro.pay.oppwa.payments.pre.auth.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Payment {

    private double amount;
    //
    private String entityId;
    private String currency;
    private String paymentType; // Enum
    private String paymentBrand; // Enum
    private String documentNumber; // CPF as requested on test
    //
    private int cardCvv;
    private int cardExpiryYear;
    private int cardExpiryMonth;
    //
    private String cardNumber;
    private String cardHolder;

}
