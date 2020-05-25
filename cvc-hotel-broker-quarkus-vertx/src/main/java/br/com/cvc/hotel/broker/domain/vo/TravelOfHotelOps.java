package br.com.cvc.hotel.broker.domain.vo;

import br.com.cvc.hotel.broker.domain.hotel.HotelInfoReq;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONArray;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Period;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
@NoArgsConstructor
public class TravelOfHotelOps {

    //private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final HttpClient httpClient = HttpClient.newBuilder()
            //.executor(executorService)
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    @SneakyThrows
    public TravelInfoResp getHotelDetails(final String url, final HotelInfoReq pHotel) {

        final var uri = new URI(url.concat(String.valueOf(pHotel.getCityCode())));
        final var request = HttpRequest.newBuilder().uri(uri).GET().build();
        final var response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .toCompletableFuture()
                .get();
        // To works with GraalVM must remove POJO's Reflections
        // and works with JSON Objects
        final var json = new JSONArray(response).getJSONObject(0);
        // Works just with first Room option
        final var jsonOfRoom = json.getJSONArray("rooms").getJSONObject(0);
        final var jsonOfPrices = json.getJSONArray("rooms").getJSONObject(0).getJSONObject("price");

        //
        final double priceOfAdult = jsonOfPrices.getDouble("adult");
        final double priceOfChild = jsonOfPrices.getDouble("child");
        //
        final int totalAdult = pHotel.getQntAdult();
        final int totalChild = pHotel.getQntChildren();
        final DecimalFormat dt = new DecimalFormat("####0.00");
        final int periodOfDays = Period.between(pHotel.getCheckIn(), pHotel.getCheckOut()).getDays();

        final double totalPrice = (((priceOfAdult * totalAdult * periodOfDays) / 0.7d) +
                ((priceOfChild * totalChild * periodOfDays) / 0.7d));

        final List<RoomInfoResp> rooms = Collections.singletonList(new RoomInfoResp() {{

            setRoomID(jsonOfRoom.getInt("roomID"));
            setCategoryName(jsonOfRoom.getString("categoryName"));
            setTotalPrice(NumberFormat.getInstance().parse(dt.format(totalPrice)).doubleValue());

            setPriceDetail(new PriceInfoResp() {{
                setPricePerDayAdult(priceOfAdult);
                setPricePerDayChild(priceOfChild);
            }});

        }});

        return new TravelInfoResp(jsonOfRoom.getInt("roomID"), json.getString("cityName"), rooms);
    }
}
