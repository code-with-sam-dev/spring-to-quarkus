package dev.codewithsam.payments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Payment {

    @Id
    @GeneratedValue
    public Long id;

    public long amountInMinorUnits;
    public String currency;

    protected Payment() {
    }

    public Payment(long amountInMinorUnits, String currency) {
        this.amountInMinorUnits = amountInMinorUnits;
        this.currency = currency;
    }
}
