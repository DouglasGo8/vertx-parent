package br.com.cvc.hotel.broker.domain.vo;

import br.com.cvc.hotel.broker.domain.hotel.HotelInfoReq;
import br.com.cvc.hotel.broker.domain.hotel.HotelsAvailsResp;
import br.com.cvc.hotel.broker.domain.hotel.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@NoArgsConstructor
public class TravelOfHotelOps {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static final HttpClient httpClient = HttpClient.newBuilder().executor(executorService)
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    @SneakyThrows
    public static TravelInfoResp getHotelDetails(final String url, final HotelInfoReq pHotel) {

        var uri = new URI(url.concat(String.valueOf(pHotel.getCityCode())));
        var request = HttpRequest.newBuilder().uri(uri).GET().build();
        var response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .toCompletableFuture()
                .get();

        var hotels = new ObjectMapper().readValue(response, HotelsAvailsResp[].class);
        final HotelsAvailsResp hotelsAvailsResp = hotels[0];

        final DecimalFormat dt = new DecimalFormat("####0.00");
        final int periodOfDays = Period.between(pHotel.getCheckIn(), pHotel.getCheckOut()).getDays();
        //
        final int totalAdult = pHotel.getQntAdult();
        final int totalChild = pHotel.getQntChildren();
        //
        final Room room = hotelsAvailsResp.getRooms().get(0);
        //
        final double totalPrice = (((room.getPrice().getAdult() * totalAdult * periodOfDays) / 0.7d) +
                ((room.getPrice().getChild() * totalChild * periodOfDays) / 0.7d));

        final List<RoomInfoResp> rooms = Collections.singletonList(new RoomInfoResp() {{
            setRoomID(hotelsAvailsResp.getId()); // roomID or hotels ID?
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
