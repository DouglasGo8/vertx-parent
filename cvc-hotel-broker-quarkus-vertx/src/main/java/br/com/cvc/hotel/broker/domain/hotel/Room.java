package br.com.cvc.hotel.broker.domain.hotel;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dbatista
 */
@Data
@NoArgsConstructor
public class Room {
    private int roomID;
    private String categoryName;
    //
    private Price price;
}
