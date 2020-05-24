package br.com.cvc.hotel.broker.api.rest;

import br.com.cvc.hotel.broker.domain.hotel.HotelInfoReq;
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
public class TravelRouteVertx extends AbstractVerticle {


    private static final Logger logger = LoggerFactory.getLogger(TravelRouteVertx.class);

    @Override
    @SneakyThrows
    public void start() {

        final Router router = Router.router(super.vertx);
        final String camelQ = super.config().getString("camelQ");
        final String vertxQ = super.config().getString("vertxQ");
        //
        final int httpPort = super.config().getInteger("httpPort", 8080);
        final ApplicationContext springContext = new
                ClassPathXmlApplicationContext(super.config().getString("appContextPath"));
        final CamelContext cvcCamelContext = springContext.getBean("cvcCamelContext", CamelContext.class);
        //
        CamelBridge.create(super.vertx, new CamelBridgeOptions(cvcCamelContext)
                .addOutboundMapping( /* from Vertx Bus */ fromVertx(vertxQ).toCamel(camelQ))).start();
        //
        router.route().handler(BodyHandler.create());
        //
        router.get(super.config().getString("apiSayHelloPath")).handler(handler -> {
            handler.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(new JsonObject().put("message", "Hi CVC")));
        });

        router.post(super.config().getString("apiTravelReservationPath"))
                .handler(this::travelReservation);

        super.vertx.createHttpServer().requestHandler(router).listen(httpPort,
                response -> {
                    if (response.succeeded()) {
                        logger.info(":: Server is now listening! :: at :: " + httpPort);
                    } else {
                        throw new IllegalArgumentException(response.cause());
                    }
                });
    }

    /**
     * @param rCtx vert.x Routing Context
     * @see https://github.com/vert-x3/vertx-examples/blob/master/core-examples/src/main/java/io/vertx/example/core/eventbus/messagecodec/util/CustomMessageCodec.java
     */
    private void travelReservation(final RoutingContext rCtx) {
        try {
            final String vertxQ = super.config().getString("vertxQ");
            // create Custom Codec to PRODUCTION deploy
            final HotelInfoReq travelInfoReq = rCtx.getBodyAsJson().mapTo(HotelInfoReq.class);
            // To only demonstration purpose, to use always json as native Vert.x implementation
            final JsonObject json = new JsonObject(Json.encode(travelInfoReq));

            super.vertx.eventBus().request(vertxQ, json, reply -> {
                if (reply.succeeded()) {
                    rCtx.response()
                            .setStatusCode(201)
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
