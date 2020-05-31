package com.primeiro.pay.oppwa.payments.preauth.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreAuthResp {
    private final String id;
    private final Result result;
}
