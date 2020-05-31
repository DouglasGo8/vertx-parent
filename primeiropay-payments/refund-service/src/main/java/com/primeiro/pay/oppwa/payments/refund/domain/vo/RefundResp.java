package com.primeiro.pay.oppwa.payments.refund.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefundResp {
    private final String id;
    private final Result result;
}
