package br.com.cvc.hotel.broker.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbPropertyOrder;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonbPropertyOrder({"id", "cityName", "rooms"})
public class TravelInfoResp {

    private int id;
    private String cityName;
    private List<RoomInfoResp> rooms;
}
