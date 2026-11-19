
package waypoint.model;

import java.time.Instant;
import java.util.Objects;

/**
 * ENTITY. Invariants:
 * I1: entry is always present
 * I2: exit is null, or not before entry
 * I3: fare is null, or not negative
 * I4: fare exists only if exit exists
 * I5: a closed ticket never reopens
 */
public final class Ticket {

    private final TicketId id;
    private final SpotId spot;
    private final Instant entry;

    private Instant exit;
    private Money fare;

    private Ticket(TicketId id, SpotId spot, Instant entry) {
        this.id = Objects.requireNonNull(id, "Ticket ID cannot be null");
        this.spot = Objects.requireNonNull(spot, "Spot ID cannot be null");
        this.entry = Objects.requireNonNull(entry, "Entry time cannot be null");
    }

    public static Ticket open(TicketId id, SpotId spot, Instant entry) {
        return new Ticket(id, spot, entry);
    }

    public boolean isOpen() {
        return exit == null;
    }

    public Money close(Instant at, FarePolicy policy) {

        Objects.requireNonNull(at, "Exit time cannot be null");
        Objects.requireNonNull(policy, "Fare policy cannot be null");

        if (!isOpen()) {
            throw new TicketAlreadyClosed("Ticket is already closed");
        }

        if (at.isBefore(entry)) {
            throw new IllegalArgumentException(
                "Exit time cannot be before entry time"
            );
        }

        Money calculatedFare = Objects.requireNonNull(
            policy.fareFor(entry, at, spot),
            "Fare cannot be null"
        );

        if (calculatedFare.isNegative()) {
            throw new IllegalArgumentException("Fare cannot be negative");
        }

        // Set exit and fare together after all validations.
        this.exit = at;
        this.fare = calculatedFare;

        return fare;
    }

    public Money fare() {
        return fare;
    }
}

class TicketAlreadyClosed extends IllegalStateException {

    TicketAlreadyClosed(String message) {
        super(message);
    }
}
