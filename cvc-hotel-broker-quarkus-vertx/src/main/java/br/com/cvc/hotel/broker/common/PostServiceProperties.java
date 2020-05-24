package br.com.cvc.hotel.broker.common;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Data;

@Data
@ConfigProperties(prefix = "hotel")
public class PostServiceProperties {
    private String url;
}
