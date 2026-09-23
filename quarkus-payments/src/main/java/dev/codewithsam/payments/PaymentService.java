package dev.codewithsam.payments;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    // The Spring service, translated line for line. No transaction anywhere.
    public Long recordLikeSpring(long amountInMinorUnits, String currency) {
        Payment payment = new Payment(amountInMinorUnits, currency);
        repository.persist(payment);
        return payment.id;
    }

    // The fix: the unit of work is declared, because persist() does not own one.
    @Transactional
    public Long record(long amountInMinorUnits, String currency) {
        Payment payment = new Payment(amountInMinorUnits, currency);
        repository.persist(payment);
        return payment.id;
    }

    public long count() {
        return repository.count();
    }
}
