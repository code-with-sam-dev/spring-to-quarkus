package dev.codewithsam.payments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.TransactionRequiredException;
import org.junit.jupiter.api.Test;

/*
 * The same shape as the Spring service, translated line for line. persist()
 * does not open a transaction of its own, so without @Transactional it throws.
 */
@QuarkusTest
class PersistNeedsATransactionTest {

    @Inject PaymentService payments;
    @Inject PaymentRepository repository;

    @Test
    void theLineForLineTranslationThrows() {
        TransactionRequiredException e = assertThrows(TransactionRequiredException.class,
            () -> payments.recordLikeSpring(10_000, "USD"));
        System.out.println("MEASURED: " + e.getClass().getName() + ": " + e.getMessage());
    }

    @Test
    void addingTransactionalWritesTheRow() {
        long before = payments.count();
        payments.record(10_000, "USD");
        assertEquals(before + 1, payments.count());
    }
}
