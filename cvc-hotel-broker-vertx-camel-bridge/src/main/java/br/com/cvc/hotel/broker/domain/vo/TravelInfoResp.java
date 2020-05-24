package br.com.cvc.hotel.broker.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TravelInfoResp {
    private final int id;
    private final String cityName;
    //
    private final List<RoomInfoResp> rooms;
}
