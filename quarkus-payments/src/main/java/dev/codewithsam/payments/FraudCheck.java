package dev.codewithsam.payments;

import io.quarkus.arc.properties.IfBuildProperty;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@IfBuildProperty(name = "payments.fraud-check.enabled", stringValue = "true")
public class FraudCheck {

    public boolean looksSuspicious(long amountInMinorUnits) {
        return amountInMinorUnits > 1_000_000;
    }
}
