package dev.codewithsam.payments;

import jakarta.enterprise.inject.Instance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

@Path("/fraud-check")
public class FraudCheckStatus {

    private final Instance<FraudCheck> fraudCheck;

    public FraudCheckStatus(Instance<FraudCheck> fraudCheck) {
        this.fraudCheck = fraudCheck;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> status() {
        return Map.of("fraudCheck", fraudCheck.isResolvable() ? "present" : "absent");
    }
}
