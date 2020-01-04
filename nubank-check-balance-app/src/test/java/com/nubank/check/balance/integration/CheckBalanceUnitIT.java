package com.nubank.check.balance.integration;


import com.jayway.restassured.RestAssured;
import org.apache.http.annotation.NotThreadSafe;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 *
 */
@NotThreadSafe
public class CheckBalanceUnitIT {


    /**
     *
     */
    @BeforeClass
    public static void setUpRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = Integer.getInteger("http.port", 32666);
    }


    /**
     *
     */
    @AfterClass
    public static void cleanUpRestAssured() {
        RestAssured.reset();
    }

    /**
     *
     */
    @Test
    public void check_01() {

        get("/api/greeting/sayHello")
                .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Hi NuBank"));

    }

    @Test
    public void check_02() {

        get("/api/greeting/dbatista")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo("30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4"))
                .body("ssn", equalTo("613-94-9711"))
                .body("customerName", equalTo("dbatista"))
                .body("email", equalTo("dbatista@nubank.com"));

    }

    @Test
    public void check_03() {

        get("/api/greeting/unknown")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo("-1"))
                .body("ssn", equalTo("0"))
                .body("customerName", equalTo("Invalid Customer"))
                .body("email", equalTo("unknown@mail"));

    }

    @Test
    public void check_04() {

        final String currentBalance =
                get("/api/status/balance/30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4/0998872-1")
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getString("currentBalance");

        assertThat(currentBalance).isEqualTo("0,00");
    }

    @Test
    public void check_05() {

        final String payload = "{\"id\": \"30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4\"," +
                " \"acc\": \"0998872-1\", \"debit\": " + 487.12d + "}";

        given().body(payload)
                .request().put("/api/account/withdraw")
                .then()
                .statusCode(201)
                .body("message", equalTo("Operation now allowed - Insufficient Balance"));
    }


    @Test
    public void check_06() {

        final String payload = "{\"id\": \"30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4\"," +
                " \"acc\": \"0998872-1\", \"credit\": " + 487.12d + "}";

        given().body(payload)
                .request().post("/api/account/add/credit")
                .then()
                .statusCode(201)
                .body("message", equalTo("Got your previous Balance was 0,00 now is 487,12"));
    }


    @Test
    public void check_07() throws Exception {

        final String payload = "{\"id\": \"30717a82-ab1b-11e9-a2a3-2a2ae2dbcce4\"," +
                " \"acc\": \"0998872-1\", \"debit\": " + 87.12d + "}";

        given().body(payload)
                .request().put("/api/account/withdraw")
                .then()
                .statusCode(201)
                .body("message", equalTo("Got it 87,12 of total 400,00"));
    }

}
