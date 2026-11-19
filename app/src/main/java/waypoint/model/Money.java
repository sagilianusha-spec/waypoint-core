
package waypoint.model;

import java.util.*;
import java.math.BigDecimal;

public final class Money implements Comparable<Money> {

    private final long paise;
    private final Currency currency;

    public static final Currency INR = Currency.getInstance("INR");
    public static final Currency USD = Currency.getInstance("USD");

    private Money(long paise, Currency currency) {
        this.paise = paise;
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
    }

    public static Money ofMinor(long amount, Currency currency) {
        return new Money(amount, currency);
    }

    public static Money rupees(long rupees) {
        return new Money(Math.multiplyExact(rupees, 100), INR);
    }

    public Money plus(Money other) {
        checkCurrency(other);
        return new Money(Math.addExact(paise, other.paise), currency);
    }

    public Money minus(Money other) {
        checkCurrency(other);
        return new Money(Math.subtractExact(paise, other.paise), currency);
    }

    public Money times(int multiplier) {
        return new Money(Math.multiplyExact(paise, multiplier), currency);
    }

    public boolean isNegative() {
        return paise < 0;
    }

    public long getPaise() {
        return paise;
    }

    public Currency getCurrency() {
        return currency;
    }

    private void checkCurrency(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");

        if (!currency.equals(other.currency)) {
            throw new CurrencyMismatch("Currencies do not match");
        }
    }

    @Override
    public int compareTo(Money other) {
        checkCurrency(other);
        return Long.compare(paise, other.paise);
    }

    public List<Money> allocate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of shares must be positive");
        }

        List<Money> shares = new ArrayList<>();

        long base = paise / n;
        long remainder = paise % n;

        for (int i = 0; i < n; i++) {
            long amount = base;

            if (remainder > 0 && i < remainder) {
                amount++;
            } else if (remainder < 0 && i < -remainder) {
                amount--;
            }

            shares.add(new Money(amount, currency));
        }

        return List.copyOf(shares);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Money other)) return false;

        return paise == other.paise &&
               currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paise, currency);
    }

    @Override
    public String toString() {
        return currency.getCurrencyCode() + " " +
               BigDecimal.valueOf(paise, 2).toPlainString();
    }
}

class CurrencyMismatch extends IllegalArgumentException {
    CurrencyMismatch(String message) {
        super(message);
    }
}
