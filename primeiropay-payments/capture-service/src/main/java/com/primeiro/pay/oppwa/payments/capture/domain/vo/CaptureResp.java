package com.primeiro.pay.oppwa.payments.capture.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CaptureResp {
    private final String id;
    private final Result result;
}
