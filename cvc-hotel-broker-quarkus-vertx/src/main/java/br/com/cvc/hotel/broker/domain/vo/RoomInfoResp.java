package br.com.cvc.hotel.broker.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbPropertyOrder;

@Data
@NoArgsConstructor
@JsonbPropertyOrder({"roomID", "categoryName", "totalPrice"})
public class RoomInfoResp {

    private int roomID;
    private String categoryName;
    private double totalPrice;
    //
    private PriceInfoResp priceDetail;
    //

}
