package com.primeiro.pay.oppwa.payments.pre.auth.api.rest;

import com.primeiro.pay.oppwa.payments.pre.auth.domain.entity.Payment;
import io.vertx.camel.CamelBridge;
import io.vertx.camel.CamelBridgeOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static io.vertx.camel.OutboundMapping.fromVertx;

/**
 * @author dbatista
 */
public class PreAuthorizationVertx extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(PreAuthorizationVertx.class);

    @Override
    @SneakyThrows
    public void start() {

        final Router router = Router.router(super.vertx);
        //
        final String camelQ = super.config().getString("camelQ");
        final String vertxQ = super.config().getString("vertxQ");
        //
        final int httpPort = super.config().getInteger("httpPort", 8080);
        final ApplicationContext springContext = new
                ClassPathXmlApplicationContext(super.config().getString("appContextPath"));
        //
        final CamelContext cvcCamelContext = springContext.getBean("preAuthCamelContext", CamelContext.class);
        //
        CamelBridge.create(super.vertx, new CamelBridgeOptions(cvcCamelContext)
                .addOutboundMapping( /* from Vertx Bus */ fromVertx(vertxQ).toCamel(camelQ))).start();
        //
        router.route().handler(BodyHandler.create());
        //
        router.get(super.config().getString("pathGreet")).handler(handler -> {
            handler.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(new JsonObject().put("message", "Hi PrimeroPay")));
        });
        //
        router.post(super.config().getString("pathPayment")).handler(this::preAuthorizationRoute);
        //
        super.vertx.createHttpServer().requestHandler(router).listen(httpPort,
                response -> {
                    if (response.succeeded()) {
                        logger.info(":: Server is now listening! :: at :: " + httpPort);
                    } else {
                        throw new IllegalArgumentException(response.cause());
                    }
                });
    }

    private void preAuthorizationRoute(final RoutingContext rCtx) {
        try {
            // Bus
            final String vertxQ = super.config().getString("vertxQ");
            //
            final Payment travelInfoReq = rCtx.getBodyAsJson().mapTo(Payment.class);
            //
            final JsonObject json = new JsonObject(Json.encode(travelInfoReq));
            //
            super.vertx.eventBus().request(vertxQ, json, reply -> {
                if (reply.succeeded()) {
                    rCtx.response()
                            .setStatusCode(200)
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(reply.result().body()));
                } else {
                    rCtx.response()
                            .setStatusCode(500)
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(reply.cause()));
                }
            });
        } catch (Exception e) {
            logger.info(e.getMessage());
            rCtx.response().end(Json.encodePrettily(e.getMessage()));
        }
    }
}
