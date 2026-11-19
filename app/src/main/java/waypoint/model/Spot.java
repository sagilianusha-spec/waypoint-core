
package waypoint.model;

import java.util.Objects;

public final class Spot {

    private final SpotId id;
    private VehicleId occupant;

    public Spot(SpotId id) {
        this.id = Objects.requireNonNull(id, "Spot ID cannot be null");
    }

    public boolean tryAssign(VehicleId v) {
        Objects.requireNonNull(v, "Vehicle cannot be null");

        if (occupant != null) {
            return false;
        }

        occupant = v;
        return true;
    }

    public void release(VehicleId v) {
        if (v != null && v.equals(occupant)) {
            occupant = null;
        }
    }

    public boolean isFree() {
        return occupant == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Spot other)) return false;

        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
