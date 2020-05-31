package com.primeiro.pay.oppwa.payments.refund.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dbatista
 */
@Data
@NoArgsConstructor
public class Refund {
    private String id;
    private String amount;
    private String entityId;
    private String currency;
    private String paymentType;
}
