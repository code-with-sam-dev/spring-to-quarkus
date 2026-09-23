package dev.codewithsam.payments;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/*
 * In Spring the property is read when the application STARTS. The same
 * compiled classes produce a different bean graph depending on what the
 * environment says at startup.
 */
class FraudCheckConditionTest {

    @Nested
    @SpringBootTest
    class WithTheDefault {
        @Autowired ApplicationContext context;

        @Test
        void theBeanIsAbsent() {
            assertThat(context.getBeanNamesForType(FraudCheck.class)).isEmpty();
        }
    }

    @Nested
    @SpringBootTest(properties = "payments.fraud-check.enabled=true")
    class TurnedOnAtStartup {
        @Autowired ApplicationContext context;

        @Test
        void theBeanExists() {
            assertThat(context.getBeanNamesForType(FraudCheck.class)).hasSize(1);
        }
    }
}
