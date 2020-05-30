package io.vertx.pagseguro.camel.bridge.rmi;

import io.vertx.camel.CamelBridge;
import io.vertx.camel.CamelBridgeOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pagseguro.utils.VertxRunner;

import org.apache.camel.CamelContext;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static io.vertx.camel.OutboundMapping.fromVertx;

/**
 * @author dbatista
 */
public class RmiMainVerticle extends AbstractVerticle {

    public static void main(String[] args) {

        VertxRunner.runVerticle(vertx -> {
            vertx.deployVerticle(RmiMainVerticle.class.getName(), new DeploymentOptions());
        });
    }

    @Override
    public void start() throws Exception {
        // TODO Auto-generated method stub

        LoggerFactory.getLogger(RmiMainVerticle.class).info("Verticle ready...");

        @SuppressWarnings("resource")
        ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");

        CamelContext camel = appContext.getBean("camel", CamelContext.class);

        CamelBridge.create(super.vertx, new CamelBridgeOptions(camel)
                .addOutboundMapping( /* from Vertx Bus */ fromVertx("invocation").toCamel("helloEndpoint")))
                .start();
        final Router router = Router.router(super.vertx);

        router.get("/api/hotel/:idCity").handler(this::cityDetails);

        super.vertx.createHttpServer().requestHandler(router).listen(8777,
                response -> {
                    if (response.succeeded()) {
                        System.out.println(":: Server is now listening! :: at :: 8777");
                    } else {
                        throw new IllegalArgumentException(response.cause());
                    }
                });

        /*vertx.createHttpServer().requestHandler(request -> {

            final String param = request.getParam("name"); // ?name=Value

            if (null == param) {
                request.response().setStatusCode(503).end(Json.encodePrettily("{\"msg\": \"error\""));
            }

            super.vertx.eventBus().<String>send("invocation", param, reply -> {
                if (reply.failed()) {
                    request.response().setStatusCode(503).end(reply.cause().getMessage());
                } else {
                    request.response().setStatusCode(200).end(reply.result().body());
                }
            });

        }).listen(8777);*/

    }

    @SuppressWarnings("deprecation")
	private void cityDetails(final RoutingContext vertxContext) {
        super.vertx.eventBus().<String>send("invocation", "hello", reply -> {
            if (reply.succeeded()) {
                vertxContext.response().end(Json.encodePrettily(reply.result().body()));
            } else {

                vertxContext.response().end(Json.encodePrettily(reply.cause()));
            }
        });
    }

}
