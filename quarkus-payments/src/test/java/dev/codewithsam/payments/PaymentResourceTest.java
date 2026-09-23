package dev.codewithsam.payments;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PaymentResourceTest {

    @Test
    void postCreatesAndGetReadsBack() {
        String location = given().contentType("application/json")
            .body("{\"amountInMinorUnits\":10000,\"currency\":\"USD\"}")
            .when().post("/payments")
            .then().statusCode(201).extract().header("Location");

        given().when().get(location)
            .then().statusCode(200)
            .body("amountInMinorUnits", is(10000))
            .body("feeInMinorUnits", is(150));
    }

    @Test
    void anUnknownIdIs404() {
        given().when().get("/payments/999999").then().statusCode(404);
    }
}
