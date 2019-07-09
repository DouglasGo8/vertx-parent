package io.pagseguro.vertx.kafka.service;

import java.util.Collections;

import org.apache.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.kafka.client.consumer.KafkaReadStream;
import lombok.NoArgsConstructor;

/**
 * 
 * @author dbatista
 *
 */
@NoArgsConstructor
public class DashboardVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		Logger.getLogger(DashboardVerticle.class).info("DashboardVerticle ready...");

		final JsonObject config = config();
		final JsonObject dashboard = new JsonObject();
		
		final Router router = Router.router(super.vertx);
		final BridgeOptions options = new BridgeOptions();
		//
		options.setOutboundPermitted(Collections.singletonList(new PermittedOptions().setAddress("dashboard")));
		//
		router.route("/eventbus/*").handler(SockJSHandler.create(super.vertx).bridge(options));
		router.route().handler(StaticHandler.create().setCachingEnabled(false));
		//
		HttpServer httpServer = super.vertx.createHttpServer();

		httpServer.requestHandler(router).listen(8085, ar -> {
			if (ar.succeeded()) {
				Logger.getLogger(DashboardVerticle.class).info("Http Server started...");
			} else {
				ar.cause().printStackTrace();
			}
		});

		
		vertx.setPeriodic(1000, timerID -> {
			vertx.eventBus().publish("dashboard", dashboard);
		});
		/**
		 * 	
		 */
		final KafkaReadStream<String, JsonObject> consumer = 
					KafkaReadStream.create(vertx, config.getMap(), String.class, JsonObject.class);

		consumer.handler(record -> {

			final JsonObject obj = record.value();
			dashboard.mergeIn(obj);
		});

		consumer.subscribe(Collections.singleton("the_topic"));
	}
}
