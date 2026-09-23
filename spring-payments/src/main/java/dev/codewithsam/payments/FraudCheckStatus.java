package dev.codewithsam.payments;

import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FraudCheckStatus {

    private final ObjectProvider<FraudCheck> fraudCheck;

    public FraudCheckStatus(ObjectProvider<FraudCheck> fraudCheck) {
        this.fraudCheck = fraudCheck;
    }

    @GetMapping("/fraud-check")
    public Map<String, String> status() {
        return Map.of("fraudCheck", fraudCheck.getIfAvailable() != null ? "present" : "absent");
    }
}
