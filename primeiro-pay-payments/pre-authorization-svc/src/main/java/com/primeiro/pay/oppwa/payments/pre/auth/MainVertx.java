package com.primeiro.pay.oppwa.payments.pre.auth;

import com.primeiro.pay.oppwa.payments.pre.auth.api.rest.PreAuthorizationVertx;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import lombok.SneakyThrows;

import static io.vertx.pagseguro.VertxRunner.runVerticle;

/**
 * @author dbatista
 */
public class MainVertx extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MainVertx.class);

    @Override
    @SneakyThrows
    public void start() throws Exception {
        runVerticle(vertx -> ConfigRetriever.create(vertx, new ConfigRetrieverOptions() {
            {
                addStore(new ConfigStoreOptions() {
                    {
                        setType("file");
                        setFormat("json");
                        setConfig(new JsonObject().put("path", "./src/main/resources/conf/config.json"));
                    }
                });
            }
        }).getConfig(ar -> {
            if (ar.failed()) {
                throw new IllegalArgumentException(ar.cause());
            } else {

                logger.info("**** MainVertx ready... ****");

                vertx.deployVerticle(PreAuthorizationVertx.class.getName(),
                        new DeploymentOptions().setConfig(ar.result()));
            }
        }));
    }
}
