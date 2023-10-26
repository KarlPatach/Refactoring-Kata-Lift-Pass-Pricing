package dojo.liftpasspricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import spark.Spark;

public class PricesTest {

    private static Connection connection;

    @BeforeAll
    public static void createPrices() throws SQLException {
        connection = Prices.createApp();
    }

    @AfterAll
    public static void stopApplication() throws SQLException {
        Spark.stop();
        connection.close();
    }

    @Test
    void verifyBasePrice() {
        JsonPath response = RestAssured.
            given().
                port(4567).
            when().
                // construct some proper url parameters
                get("/prices?type=1jour").
            then().
                assertThat().
                    statusCode(200).
                assertThat().
                    contentType("application/json").
            extract().jsonPath();

        assertEquals(35, response.getInt("cost"));
    }

    @Test
    void verifyIsFreeForChildren() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=1jour&age=2").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(0, response.getInt("cost"));
    }

    @Test
    void verifyTeenagerPrice() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=1jour&age=12").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(25, response.getInt("cost"));
    }

    @Test
    void verifySeniorPrice() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=1jour&age=70").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(27, response.getInt("cost"));
    }

    @Test
    void verifyBasePriceWithAge() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=1jour&age=42").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(35, response.getInt("cost"));
    }

    @Test
    void verifyNightChildrenFree() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=night&age=2").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(0, response.getInt("cost"));
    }

    @Test
    void verifyNightNoAgeFree() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=night").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(0, response.getInt("cost"));
    }

    @Test
    void verifyHoliday() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=1jour&date=2019-02-18").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(35, response.getInt("cost"));
    }

    @Test
    void verifyNoHoliday() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=1jour&date=2020-02-20").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(35, response.getInt("cost"));
    }

    @Test
    void verifyMondayPrices() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=1jour&date=2023-10-23").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(23, response.getInt("cost"));
    }

    @Test
    void verifyNightSeniorPrice() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=night&age=70").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(8, response.getInt("cost"));
    }

    @Test
    void verifyNightBasePrice() {
        JsonPath response = RestAssured.
                given().
                port(4567).
                when().
                // construct some proper url parameters
                        get("/prices?type=night&age=42").
                then().
                assertThat().
                statusCode(200).
                assertThat().
                contentType("application/json").
                extract().jsonPath();

        assertEquals(19, response.getInt("cost"));
    }
}
