package dev.codewithsam.payments;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public Long record(long amountInMinorUnits, String currency) {
        return repository.save(new Payment(amountInMinorUnits, currency)).getId();
    }
}
