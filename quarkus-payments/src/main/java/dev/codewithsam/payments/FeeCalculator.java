package dev.codewithsam.payments;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class FeeCalculator {

    private final int feeBasisPoints;

    public FeeCalculator(@ConfigProperty(name = "payments.fee-basis-points") int feeBasisPoints) {
        this.feeBasisPoints = feeBasisPoints;
    }

    public long feeFor(long amountInMinorUnits) {
        return amountInMinorUnits * feeBasisPoints / 10_000;
    }
}
