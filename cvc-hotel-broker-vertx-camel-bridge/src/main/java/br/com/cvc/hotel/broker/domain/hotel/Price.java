package br.com.cvc.hotel.broker.domain.hotel;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dbatista
 */
@Data
@NoArgsConstructor
public class Price {

    private double adult;
    private double child;
}
