package dev.codewithsam.payments;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final FeeCalculator fees;

    public PaymentService(PaymentRepository repository, FeeCalculator fees) {
        this.repository = repository;
        this.fees = fees;
    }

    public Long record(long amountInMinorUnits, String currency) {
        return repository.save(new Payment(amountInMinorUnits, currency)).getId();
    }

    public Optional<PaymentView> find(Long id) {
        return repository.findById(id).map(p -> new PaymentView(
            p.getId(), p.getAmountInMinorUnits(), p.getCurrency(), fees.feeFor(p.getAmountInMinorUnits())));
    }
}
