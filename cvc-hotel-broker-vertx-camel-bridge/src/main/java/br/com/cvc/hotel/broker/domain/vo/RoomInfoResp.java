package br.com.cvc.hotel.broker.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomInfoResp {

    private int roomID;
    private String categoryName;
    private double totalPrice;
    //
    private PriceInfoResp priceDetail;
    //

}
