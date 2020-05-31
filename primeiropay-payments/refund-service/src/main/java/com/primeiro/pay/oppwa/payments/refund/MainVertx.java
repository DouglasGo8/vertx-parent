package com.primeiro.pay.oppwa.payments.refund;

import com.primeiro.pay.oppwa.payments.refund.api.rest.RefundVertx;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.pagseguro.utils.VertxRunner.runVerticle;

/**
 * @author dbatista
 */
public class MainVertx extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVertx.class);

    @Override
    @SneakyThrows
    public void start() {
        //
        runVerticle(vertx -> ConfigRetriever.create(vertx, new ConfigRetrieverOptions() {
            {
                addStore(new ConfigStoreOptions() {
                    {
                        setType("file");
                        setFormat("json");
                        setConfig(new JsonObject().put("path", "./config.json"));
                    }
                });
            }
        }).getConfig(ar -> {
            if (ar.failed()) {
                throw new IllegalArgumentException(ar.cause());
            } else {
                logger.info("**** MainVertx ready... ****");
                vertx.deployVerticle(RefundVertx::new, new DeploymentOptions().setConfig(ar.result()));
            }
        }), 10);
    }
}
