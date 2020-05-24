package br.com.cvc.hotel.broker.domain.hotel;

import lombok.Data;

import java.util.List;

@Data
public class HotelsAvailsResp {
    private int id;
    private String name;
    private int cityCode;
    private String cityName;
    private List<Room> rooms;
}
