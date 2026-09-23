package dev.codewithsam.payments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    private long amountInMinorUnits;
    private String currency;

    protected Payment() {
    }

    public Payment(long amountInMinorUnits, String currency) {
        this.amountInMinorUnits = amountInMinorUnits;
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }
}
