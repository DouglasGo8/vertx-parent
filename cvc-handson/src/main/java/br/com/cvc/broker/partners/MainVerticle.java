package br.com.cvc.broker.partners;

import br.com.cvc.broker.partners.api.rest.HotelsBrokerVerticleProxy;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import lombok.SneakyThrows;

/**
 * Main verticle
 * ready to start spring beans to use
 * as bridge with Vert.x and Apache Camel
 * @see new ClassPathXmlApplicationContext()
 */
public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    @SneakyThrows
    public void start() {

        logger.info("**** Main Verticle ready to load Configurations... ****");


        super.vertx.deployVerticle(HotelsBrokerVerticleProxy.class.getName(),
                new DeploymentOptions().setConfig(super.config()));

    }
}
