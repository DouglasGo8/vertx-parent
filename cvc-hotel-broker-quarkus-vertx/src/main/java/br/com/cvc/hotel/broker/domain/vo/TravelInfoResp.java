package br.com.cvc.hotel.broker.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelInfoResp {
    private  int id;
    private  String cityName;
    private  List<RoomInfoResp> rooms;
}
