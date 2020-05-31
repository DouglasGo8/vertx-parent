package com.primeiro.pay.oppwa.payments.pre.auth.domain.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Payment {

    private String amount;
    //
    private String currency;
    private String paymentType; // Enum
    private String paymentBrand; // Enum
    private String documentNumber; // CPF as requested on test
    //
    private String cardCvv;
    private String cardExpiryYear;
    private String cardExpiryMonth;
    //
    private String cardNumber;
    private String cardHolder;

}
