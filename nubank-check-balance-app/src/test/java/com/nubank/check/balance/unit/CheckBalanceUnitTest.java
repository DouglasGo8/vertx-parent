package com.nubank.check.balance.unit;


import com.nubank.check.balance.api.rest.CheckBalanceServiceVerticle;
import com.nubank.check.balance.domain.TransactionAccount;
import com.nubank.check.balance.infrastructure.database.CheckBalanceRepoVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

/**
 * @author nubank
 */
@RunWith(VertxUnitRunner.class)
@SuppressWarnings("deprecation")
public class CheckBalanceUnitTest {


    private int port;
    private Vertx vertx;

    @Before
    public void setUpVerticles(TestContext context) throws IOException {

        this.vertx = Vertx.vertx();

        try (final ServerSocket socket = new ServerSocket(0)) {

            this.port = socket.getLocalPort();

            final DeploymentOptions restApiOption = new DeploymentOptions()
                    .setConfig(new JsonObject().put("http.port", this.port));


            vertx.deployVerticle(CheckBalanceRepoVerticle.class.getName(),
                    new DeploymentOptions(), context.asyncAssertSuccess());

            vertx.deployVerticle(CheckBalanceServiceVerticle.class.getName(),
                    restApiOption, context.asyncAssertSuccess());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @After
    public void tearDownVerticles(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testSaysHiNuBank(TestContext context) {

        final Async async = context.async();

        vertx.createHttpClient().get(this.port, "localhost", "/api/greeting/sayHello")
                .handler(response -> {
                    context.assertEquals(response.statusCode(), 200);
                    response.bodyHandler(body -> {
                        context.assertEquals("Hi NuBank", body.toJsonObject().getString("message"));
                        async.complete();
                    });
                }).end();

    }

    @Test
    public void testGreetingDefaultCustomer(TestContext context) {

        final Async async = context.async();

        vertx.createHttpClient().get(this.port, "localhost", "/api/greeting/dbatista")
                .handler(response -> {
                    context.assertEquals(response.statusCode(), 200);
                    response.bodyHandler(body -> {
                        context.assertEquals("30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4",
                                body.toJsonObject().getString("id"));
                        context.assertEquals("613-94-9711", body.toJsonObject().getString("ssn"));
                        context.assertEquals("dbatista", body.toJsonObject().getString("customerName"));
                        context.assertEquals("dbatista@nubank.com",
                                body.toJsonObject().getString("email"));
                        async.complete();
                    });

                }).end();
    }

    @Test
    public void testGreetingUnknownCustomer(TestContext context) {

        final Async async = context.async();

        vertx.createHttpClient().get(this.port, "localhost", "/api/greeting/unknown")
                .handler(response -> {
                    context.assertEquals(response.statusCode(), 200);
                    response.bodyHandler(body -> {
                        context.assertEquals("-1", body.toJsonObject().getString("id"));
                        context.assertEquals("0", body.toJsonObject().getString("ssn"));
                        context.assertEquals("Invalid Customer",
                                body.toJsonObject().getString("customerName"));
                        context.assertEquals("unknown@mail", body.toJsonObject().getString("email"));
                        async.complete();
                    });
                }).end();
    }

    @Test
    public void testDefaultCurrentBalanceInZero(TestContext context) {

        final Async async = context.async();

        vertx.createHttpClient().get(this.port, "localhost",
                "/api/status/balance/30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4/0998872-1")
                .handler(response -> {
                    context.assertEquals(response.statusCode(), 200);
                    response.bodyHandler(body -> {
                        context.assertEquals("0,00", body.toJsonObject().getString("currentBalance"));
                        async.complete();
                    });
                }).end();
    }

    @Test
    public void testAddCreditToDefaultCustomer(TestContext context) {

        final Async async = context.async();

        final String json = Json.encodePrettily(new TransactionAccount() {{
            setId("30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4");
            setAcc("0998872-1");
            setCredit(487.12d);
        }});

        final String length = Integer.toString(json.length());

        vertx.createHttpClient()
                .post(this.port, "localhost", "/api/account/add/credit")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", length)
                .handler(response -> {
                    context.assertEquals(response.statusCode(), 201);
                    context.assertTrue(response.headers().get("content-type").contains("application/json"));
                    response.bodyHandler(body -> {
                        context.assertEquals("Got your previous Balance was 0,00 now is 487,12",
                                body.toJsonObject().getString("message"));
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

    @Test
    public void testTryWithdrawWithoutBalance(TestContext context) {

        final Async async = context.async();

        final String json = Json.encodePrettily(new TransactionAccount() {{
            setId("30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4");
            setAcc("0998872-1");
            setDebit(487.12d);
        }});

        final String length = Integer.toString(json.length());

        vertx.createHttpClient()
                .put(this.port, "localhost", "/api/account/withdraw")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", length)
                .handler(response -> {
                    context.assertEquals(response.statusCode(), 201);
                    context.assertTrue(response.headers().get("content-type").contains("application/json"));
                    response.bodyHandler(body -> {
                        context.assertEquals("Operation now allowed - Insufficient Balance",
                                body.toJsonObject().getString("message"));
                        async.complete();
                    });
                })
                .write(json)
                .end();
    }

    @Test
    public void testTryWithdrawWithBalance(TestContext context) throws Exception {

        final Async async = context.async();

        final String jsonCredit = Json.encodePrettily(new TransactionAccount() {{
            setId("30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4");
            setAcc("0998872-1");
            setCredit(487.12d);
        }});

        final String jsonDebit = Json.encodePrettily(new TransactionAccount() {{
            setId("30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4");
            setAcc("0998872-1");
            setDebit(87.16d);
        }});


        final String lengthCredit = Integer.toString(jsonCredit.length());

        vertx.createHttpClient()
                .post(this.port, "localhost", "/api/account/add/credit")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", lengthCredit)
                .handler(response -> {
                    context.assertEquals(response.statusCode(), 201);
                    context.assertTrue(response.headers().get("content-type").contains("application/json"));
                    response.bodyHandler(body -> {
                        context.assertEquals("Got your previous Balance was 0,00 now is 487,12",
                                body.toJsonObject().getString("message"));
                        // System.out.println(body.toString());
                        async.complete();
                    });
                })
                .write(jsonCredit)
                .end();


        TimeUnit.SECONDS.sleep(1);
        final String lengthDebit = Integer.toString(jsonDebit.length());

        vertx.createHttpClient()
                .put(this.port, "localhost", "/api/account/withdraw")
                .putHeader("content-type", "application/json")
                .putHeader("content-length", lengthDebit)
                .handler(response -> {
                    context.assertEquals(response.statusCode(), 201);
                    context.assertTrue(response.headers().get("content-type").contains("application/json"));
                    response.bodyHandler(body -> {
                        context.assertEquals("Got it 87,16 of total 399,96",
                                body.toJsonObject().getString("message"));
                        async.complete();
                    });
                })
                .write(jsonDebit)
                .end();
    }


}
