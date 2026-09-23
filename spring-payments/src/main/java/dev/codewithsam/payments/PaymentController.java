package dev.codewithsam.payments;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService payments;

    public PaymentController(PaymentService payments) {
        this.payments = payments;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody NewPayment request) {
        Long id = payments.record(request.amountInMinorUnits(), request.currency());
        return ResponseEntity.created(URI.create("/payments/" + id)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentView> find(@PathVariable Long id) {
        return ResponseEntity.of(payments.find(id));
    }

    public record NewPayment(long amountInMinorUnits, String currency) {
    }
}
