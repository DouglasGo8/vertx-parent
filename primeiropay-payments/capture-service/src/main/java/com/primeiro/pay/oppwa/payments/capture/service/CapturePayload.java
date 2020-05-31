package com.primeiro.pay.oppwa.payments.capture.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.primeiro.pay.oppwa.payments.capture.domain.vo.CaptureResp;
import com.primeiro.pay.oppwa.payments.capture.domain.vo.Result;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.SneakyThrows;

/**
 * @author dbatista
 */
public class CapturePayload {


    public String makeUri(final String captureUrl, final JsonObject json) {
        return captureUrl.trim().concat(json.getString("id").trim());
    }

    public String makeHttpRequest(final JsonObject json) {
        return "entityId=" + json.getString("entityId") +
                "&amount=" + json.getString("amount") +
                "&currency=" + json.getString("currency") +
                "&paymentType=" + json.getString("paymentType");
    }

    @SneakyThrows
    public JsonObject makeHttpResponse(final String body) {

        final JsonNode json = new ObjectMapper().readTree(body);
        //
        final Result result = new Result() {{
            setCode(json.get("result").get("code").asText());
            setDescription(json.get("result").get("description").asText());
        }};
        //
        return new JsonObject(Json.encode(new CaptureResp(json.get("id").asText(), result)));
    }


}
