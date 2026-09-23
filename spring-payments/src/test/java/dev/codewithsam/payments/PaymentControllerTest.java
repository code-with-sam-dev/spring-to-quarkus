package dev.codewithsam.payments;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {

    @LocalServerPort int port;
    final HttpClient http = HttpClient.newHttpClient();

    @Test
    void postCreatesAndGetReadsBack() throws Exception {
        HttpResponse<String> created = http.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/payments"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"amountInMinorUnits\":10000,\"currency\":\"USD\"}")).build(),
            HttpResponse.BodyHandlers.ofString());
        assertThat(created.statusCode()).isEqualTo(201);
        String location = created.headers().firstValue("Location").orElseThrow();

        HttpResponse<String> read = http.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + location)).GET().build(),
            HttpResponse.BodyHandlers.ofString());
        assertThat(read.statusCode()).isEqualTo(200);
        assertThat(read.body()).contains("\"amountInMinorUnits\":10000").contains("\"feeInMinorUnits\":150");
    }

    @Test
    void anUnknownIdIs404() throws Exception {
        HttpResponse<String> read = http.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/payments/999999")).GET().build(),
            HttpResponse.BodyHandlers.ofString());
        assertThat(read.statusCode()).isEqualTo(404);
    }
}
