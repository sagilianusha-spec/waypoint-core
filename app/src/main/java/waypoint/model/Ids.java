
package waypoint.model;

import java.util.Objects;

record TicketId(String value) {
    TicketId {
        Objects.requireNonNull(value, "TicketId cannot be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("TicketId cannot be blank");
        }
    }
}

record SpotId(String value) {
    SpotId {
        Objects.requireNonNull(value, "SpotId cannot be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("SpotId cannot be blank");
        }
    }
}

record VehicleId(String value) {
    VehicleId {
        Objects.requireNonNull(value, "VehicleId cannot be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException("VehicleId cannot be blank");
        }
    }
}
