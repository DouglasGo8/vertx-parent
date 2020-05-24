package br.com.cvc.hotel.broker.service;

import br.com.cvc.hotel.broker.common.PostServiceProperties;
import br.com.cvc.hotel.broker.domain.hotel.HotelInfoReq;
import br.com.cvc.hotel.broker.domain.vo.TravelOfHotelOps;
import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import lombok.SneakyThrows;

import javax.inject.Inject;

public class HotelResource {

    @Inject
    PostServiceProperties props;

    @SneakyThrows
    @ConsumeEvent(value = "vertx.hotelByIdQ")
    public void travelReservationAsync(Message<HotelInfoReq> hotelInfoReq) {
        var travel = TravelOfHotelOps.getHotelDetails(props.getUrl(), hotelInfoReq.body());
        hotelInfoReq.reply(JsonObject.mapFrom(travel));
    }

}
