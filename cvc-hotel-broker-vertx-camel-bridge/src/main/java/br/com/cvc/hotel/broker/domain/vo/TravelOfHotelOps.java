package br.com.cvc.hotel.broker.domain.vo;

import br.com.cvc.hotel.broker.domain.hotel.HotelsAvailsResp;
import br.com.cvc.hotel.broker.domain.hotel.Room;
import io.vertx.core.json.JsonObject;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor
public class TravelOfHotelOps {

    @SneakyThrows
    public static TravelInfoResp getHotelDetails(final String url, final JsonObject json) {

        final Client restClient = ClientBuilder.newClient();

        final HotelsAvailsResp[] response = restClient.target(url)
                .path(String.valueOf(json.getInteger("cityCode"))).request(MediaType.APPLICATION_JSON)
                .get(HotelsAvailsResp[].class);
        //
        final LocalDate dtCheckIn = LocalDate.parse(json.getString("checkIn"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        //
        final LocalDate dtCheckOut = LocalDate.parse(json.getString("checkOut"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        final HotelsAvailsResp hotelsAvailsResp = Stream.of(response)
                .parallel().filter(h -> h.getId() == 1).findFirst().orElseGet(HotelsAvailsResp::new);

        final DecimalFormat dt = new DecimalFormat("####0.00");
        final int periodOfDays = Period.between(dtCheckIn, dtCheckOut).getDays();
        //
        final int totalAdult = json.getInteger("qntAdult");
        final int totalChild = json.getInteger("qntChildren");
        //
        final Room room = hotelsAvailsResp.getRooms().get(0);
        //
        final double totalPrice = (((room.getPrice().getAdult() * totalAdult * periodOfDays) / 0.7d) +
                ((room.getPrice().getChild() * totalChild * periodOfDays) / 0.7d));

        final List<RoomInfoResp> rooms = Collections.singletonList(new RoomInfoResp() {{
            setRoomID(hotelsAvailsResp.getId());
            setCategoryName(room.getCategoryName());
            setTotalPrice(NumberFormat.getInstance().parse(dt.format(totalPrice)).doubleValue());
            setPriceDetail(new PriceInfoResp() {{
                setPricePerDayAdult(room.getPrice().getAdult());
                setPricePerDayChild(room.getPrice().getChild());
            }});
        }});

        return new TravelInfoResp(hotelsAvailsResp.getId(), hotelsAvailsResp.getCityName(), rooms);
    }
}
