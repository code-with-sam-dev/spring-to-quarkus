package dev.codewithsam.payments;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
 * PaymentService.record has no @Transactional anywhere. Spring Data's save()
 * opens its own transaction, so the row is written anyway.
 */
@SpringBootTest
class SaveOwnsTheTransactionTest {

    @Autowired PaymentService payments;
    @Autowired PaymentRepository repository;

    @Test
    void theRowIsWrittenWithoutAnyTransactionalAnnotation() {
        long before = repository.count();
        payments.record(10_000, "USD");
        assertThat(repository.count()).isEqualTo(before + 1);
    }
}
