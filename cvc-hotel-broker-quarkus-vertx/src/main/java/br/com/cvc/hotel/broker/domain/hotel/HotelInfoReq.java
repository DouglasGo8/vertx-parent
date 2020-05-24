package br.com.cvc.hotel.broker.domain.hotel;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author dbatista
 */
@Data
@NoArgsConstructor
public class HotelInfoReq {

    //
    @NotNull
    private int cityCode;
    @NotNull
    private int qntAdult;
    @NotNull
    private int qntChildren;

    @NotNull
    @JsonbDateFormat(value = "dd-MM-yyyy")
    private LocalDate checkIn;
    @NotNull
    @JsonbDateFormat(value = "dd-MM-yyyy")
    private LocalDate checkOut;

}
