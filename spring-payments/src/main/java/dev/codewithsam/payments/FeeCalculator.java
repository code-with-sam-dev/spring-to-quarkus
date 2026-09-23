package dev.codewithsam.payments;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeeCalculator {

    private final int feeBasisPoints;

    public FeeCalculator(@Value("${payments.fee-basis-points}") int feeBasisPoints) {
        this.feeBasisPoints = feeBasisPoints;
    }

    public long feeFor(long amountInMinorUnits) {
        return amountInMinorUnits * feeBasisPoints / 10_000;
    }
}
