package dev.codewithsam.payments;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/*
 * The build ran with payments.fraud-check.enabled=false, so the decision about
 * FraudCheck was made during augmentation. The endpoint reports what exists.
 */
@QuarkusTest
class FraudCheckBuildPropertyTest {

    @Test
    void theBeanIsAbsentWhenTheBuildSaidFalse() {
        given().when().get("/fraud-check")
            .then().statusCode(200)
            .body("fraudCheck", is("absent"));
    }
}
