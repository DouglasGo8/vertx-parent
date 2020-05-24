package br.com.cvc.hotel.broker.service;

import br.com.cvc.hotel.broker.domain.vo.TravelOfHotelOps;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.camel.Exchange;

@NoArgsConstructor
public class TravelOfHotelByCityBean {

    @SneakyThrows
    public JsonObject messageIn(final JsonObject json, final Exchange exchange) {
        //
        final String url = exchange.getProperty("urlHotelByCity", String.class);
        return new JsonObject(Json.encode(TravelOfHotelOps.getHotelDetails(url, json)));
    }
}
