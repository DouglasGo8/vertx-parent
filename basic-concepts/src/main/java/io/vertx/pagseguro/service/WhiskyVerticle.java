package io.vertx.pagseguro.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.pagseguro.domain.Whisky;
import lombok.NoArgsConstructor;

/**
 * 
 * @author dbatista
 *
 */
@NoArgsConstructor
public class WhiskyVerticle extends AbstractVerticle {

	private MongoClient mongo;
	public static final String COLLECTION = "whiskies";
	private static final Logger logger = LoggerFactory.getLogger(WhiskyVerticle.class);

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

		LoggerFactory.getLogger(WhiskyVerticle.class).info("WhiskyVerticle ready...");

		mongo = MongoClient.createShared(super.vertx, super.config());

		final Router router = Router.router(vertx);

		/**
		 * Must enable before of router.post to handle POST bodies
		 */
		router.route().handler(BodyHandler.create());
		router.route("/api/greeting").handler(this::greeting);
		router.route("/assets/*").handler(StaticHandler.create("assets"));

		/**
		 * 
		 */
		router.get("/api/whiskies").handler(this::getAll);
		router.get("/api/whiskies/:id").handler(this::getOne);
		/**
		 * 
		 */
		router.post("/api/whiskies").handler(this::addOne);
		router.put("/api/whiskies/:id").handler(this::updateOne);
		router.delete("/api/whiskies/:id").handler(this::deleteOne);
		/**
		 * 
		 */
		super.vertx.createHttpServer().requestHandler(router).listen(super.config().getInteger("http.port", 8080),
				ar -> {
					if (ar.succeeded()) {
						logger.info("Server is now listening!");
					} else {
						logger.info(String.format("fail %s", ar.cause()));
					}
				});
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		this.mongo.close();
	}

	/**
	 * 
	 * @param routingContext
	 */
	private void greeting(RoutingContext routingContext) {

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(new JsonObject().put("msg", "Hello from Vertx")));
	}

	/**
	 * 
	 * @param routingContext
	 */
	private void addOne(RoutingContext routingContext) {

		final Whisky whisky = Json.decodeValue(routingContext.getBodyAsString(), Whisky.class);

		this.mongo.insert(COLLECTION, whisky.toJson(), r -> {

			routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(whisky.setId(r.result())));

		});
	}

	/**
	 * 
	 * @param routingContext
	 */
	@SuppressWarnings("deprecation")
	private void updateOne(RoutingContext routingContext) {

		final String id = routingContext.request().getParam("id");
		JsonObject json = routingContext.getBodyAsJson();

		if (id == null || json == null) {

			routingContext.response().setStatusCode(400).end();

		} else {
			mongo.update(COLLECTION, new JsonObject().put("_id", id), new JsonObject().put("$set", json), v -> {
				if (v.failed()) {
					routingContext
						.response()
					.setStatusCode(404)
					.end();
				} else {
					routingContext
						.response()
						.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(new Whisky(id, json.getString("name"), json.getString("origin"))));
				}
			});
		}
	}

	/**
	 * 
	 * @param routingContext
	 */
	private void getOne(RoutingContext routingContext) {

		final String id = routingContext.request().getParam("id");

		if (null == id) {
			routingContext.response().setStatusCode(400).end();
		} else {

			this.mongo.findOne(COLLECTION, new JsonObject().put("_id", id), null, ar -> {
				if (ar.succeeded()) {
					if (null == ar.result()) {
						routingContext.response().setStatusCode(404).end();
						return;
					}

					final Whisky whisky = new Whisky(ar.result());

					routingContext.response().setStatusCode(200)
							.putHeader("content-type", "application/json; charset=utf-8")
							.end(Json.encodePrettily(whisky));

				} else {
					routingContext.response().setStatusCode(404).end();
				}
			});
		}

	}
	
	@SuppressWarnings("deprecation")
	private void deleteOne(RoutingContext routingContext) {
		
		final String id = routingContext.request().getParam("id");
		
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			mongo.removeOne(COLLECTION, new JsonObject().put("_id", id),
					ar -> routingContext.response().setStatusCode(204).end());
		}
	}

	/**
	 * 
	 * @param routingContext
	 */
	private void getAll(RoutingContext routingContext) {

		this.mongo.find(COLLECTION, new JsonObject(), json -> {
			if (json.failed()) {

				routingContext.response().end(String.format("Damn %s", json.cause()));

			} else {

				// json.result().stream().map(Whisky::new) .forEach(System.out::println);

				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.end(Json.encodePrettily(json.result().stream().map(Whisky::new).collect(Collectors.toList())));
			}
		});
	}
}
