package dev.codewithsam.payments;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class PaymentService {

    private final PaymentRepository repository;
    private final FeeCalculator fees;

    public PaymentService(PaymentRepository repository, FeeCalculator fees) {
        this.repository = repository;
        this.fees = fees;
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

    public Optional<PaymentView> find(Long id) {
        return repository.findByIdOptional(id).map(p -> new PaymentView(
            p.id, p.amountInMinorUnits, p.currency, fees.feeFor(p.amountInMinorUnits)));
    }
}
