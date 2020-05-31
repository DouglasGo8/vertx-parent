package com.primeiro.pay.oppwa.payments.pre.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.primeiro.pay.oppwa.payments.pre.auth.domain.vo.PreAuthResp;
import com.primeiro.pay.oppwa.payments.pre.auth.domain.vo.Result;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.SneakyThrows;


/**
 * @author dbatista
 */
public class PreAuthPayload {

    public String makeHttpRequest(final JsonObject json, final String entityId) {

        return "entityId=" + entityId +
                "&amount=" + json.getString("amount") +
                "&currency=" + json.getString("currency") +
                "&paymentBrand=" + json.getString("paymentBrand") +
                "&paymentType=" + json.getString("paymentType") +
                "&card.number=" + json.getString("cardNumber") +
                "&card.holder=" + json.getString("cardHolder") +
                "&card.expiryMonth=" + json.getString("cardExpiryMonth") +
                "&card.expiryYear=" + json.getString("cardExpiryYear") +
                "&card.cvv=" + json.getString("cardCvv");

    }


    @SneakyThrows
    public JsonObject makeHttpResponse(final String body) {

        final JsonNode json = new ObjectMapper().readTree(body);
        final Result result = new Result() {{
           setCode(json.get("result").get("code").asText());
           setDescription(json.get("result").get("description").asText());
        }};

        final PreAuthResp resp = new PreAuthResp(json.get("id").asText(), result);

        return new JsonObject(Json.encode(resp));
    }

}
