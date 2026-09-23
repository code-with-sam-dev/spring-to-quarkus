package dev.codewithsam.payments;

public record PaymentView(Long id, long amountInMinorUnits, String currency, long feeInMinorUnits) {
}
