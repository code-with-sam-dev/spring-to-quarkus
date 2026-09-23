package dev.codewithsam.payments;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "payments.fraud-check.enabled", havingValue = "true")
public class FraudCheck {

    public boolean looksSuspicious(long amountInMinorUnits) {
        return amountInMinorUnits > 1_000_000;
    }
}
