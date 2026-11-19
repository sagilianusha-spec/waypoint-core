
package waypoint.model;

import java.util.Locale;
import java.util.Objects;

public record Registration(String value) {

    public Registration {
        Objects.requireNonNull(value, "Registration cannot be null");

        value = value.trim().toUpperCase(Locale.ROOT);

        if (!value.matches("[A-Z]{2}\\d{2}[A-Z]{1,2}\\d{4}")) {
            throw new IllegalArgumentException("Invalid registration number");
        }
    }
}
