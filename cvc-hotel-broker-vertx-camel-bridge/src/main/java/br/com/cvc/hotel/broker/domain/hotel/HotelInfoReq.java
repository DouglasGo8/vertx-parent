package br.com.cvc.hotel.broker.domain.hotel;


import br.com.cvc.hotel.broker.common.LocalDateDeserializer;
import br.com.cvc.hotel.broker.common.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author dbatista
 */
@Data
@NoArgsConstructor
public class HotelInfoReq {

    //
    private int cityCode;
    private int qntAdult;
    private int qntChildren;

    /**
     * Des/Serialization mandatory
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate checkIn;
    /**
     * Des/Serialization mandatory
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate checkOut;

}
