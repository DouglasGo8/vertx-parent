package com.nubank.check.balance.api.rest;

import com.nubank.check.balance.domain.EnumBalanceEvent;
import com.nubank.check.balance.domain.TransactionAccount;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 *
 */
public class CheckBalanceServiceVerticle extends AbstractVerticle {


    /**
     *
     */
    private static final Logger logger = LoggerFactory.getLogger(CheckBalanceServiceVerticle.class);

    /**
     * @throws Exception for Verticle
     */
    @Override
    public void start() throws Exception {

        logger.info("**** CheckBalanceServiceVerticle ready... ****");

        final Router router = Router.router(super.vertx);


        router.route().handler(BodyHandler.create());

        router.get("/api/greeting/sayHello").handler(handler -> {
            handler.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(new JsonObject().put("message", "Hi NuBank")));
        });

        router.get("/api/greeting/:fName").handler(this::ofGreetingsByCustomerId);
        router.get("/api/status/balance/:id/:acc").handler(this::ofStatusCheckBalance);

        router.put("/api/account/withdraw").handler(this::ofBalanceWithdraw);
        router.post("/api/account/add/credit").handler(this::ofAddCreditTransaction);


        final int httpPort = super.config().getInteger("http.port", 32666);
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
     * @param vertxContext
     */
    private void ofGreetingsByCustomerId(RoutingContext vertxContext) {

        final String fName = vertxContext.request().getParam("fName");

        final JsonObject json = new JsonObject().put("operation", "greeting").put("fName", fName);

        super.vertx.eventBus().send(EnumBalanceEvent.BALANCE_EVENT.toString(), json, response -> {
            if (response.succeeded()) {
                vertxContext
                        .response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(response.result().body()));

            } else {
                this.setDefaultInternalErrorServer(vertxContext);
            }
        });

    }

    /**
     * @param vertxContext
     */
    private void ofStatusCheckBalance(RoutingContext vertxContext) {

        final String id = vertxContext.request().getParam("id");
        final String acc = vertxContext.request().getParam("acc");

        final JsonObject json = new JsonObject()
                .put("operation", "status")
                .put("id", id)
                .put("acc", acc);


        super.vertx.eventBus().send(EnumBalanceEvent.BALANCE_EVENT.toString(), json, response -> {
            if (response.succeeded()) {
                vertxContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(response.result().body()));

            } else {
                this.setDefaultInternalErrorServer(vertxContext);
            }
        });

    }

    /**
     * @param vertxContext
     */
    private void ofBalanceWithdraw(RoutingContext vertxContext) {

        final TransactionAccount transaction = Json.decodeValue(vertxContext.getBodyAsString(),
                TransactionAccount.class);

        final JsonObject json = new JsonObject()
                .put("operation", "withdraw")
                .put("id", transaction.getId())
                .put("acc", transaction.getAcc())
                .put("debit", transaction.getDebit());


        super.vertx.eventBus().send(EnumBalanceEvent.BALANCE_EVENT.toString(), json, response -> {
            if (response.succeeded()) {
                vertxContext
                        .response()
                        .setStatusCode(201)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(response.result().body()));

            } else {
                this.setDefaultInternalErrorServer(vertxContext);
            }
        });
    }

    /**
     * @param vertContext
     */
    private void ofAddCreditTransaction(RoutingContext vertContext) {

        final TransactionAccount transaction = Json.decodeValue(vertContext.getBodyAsString(),
                TransactionAccount.class);

        final JsonObject json = new JsonObject()
                .put("operation", "add")
                .put("id", transaction.getId())
                .put("acc", transaction.getAcc())
                .put("credit", transaction.getCredit());


        super.vertx.eventBus().send(EnumBalanceEvent.BALANCE_EVENT.toString(), json, response -> {
            if (response.succeeded()) {
                vertContext.response()
                        .setStatusCode(201)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(response.result().body()));

            } else {
                this.setDefaultInternalErrorServer(vertContext);
            }
        });

    }

    /**
     * @param vertxContext Vert.x Core Context
     */
    private void setDefaultInternalErrorServer(RoutingContext vertxContext) {
        vertxContext.response()
                .setStatusCode(500)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(new JsonObject().put("message", "Internal Server Error")));
    }

}
