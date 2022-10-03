package dk.mjolner.workshop.docker.parameters.game;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipService {
    private Map<String, Ship> ships;

    public ShipService() {
        ships = new HashMap<>();
    }

    public ShipService(Map<String, Ship> ships) {
        this.ships = ships;
    }

    public ShipService(Ship... ships) {
        this.ships = new HashMap<>();
        for (Ship ship : ships) {
            this.ships.put(ship.getDisplayName(), ship);
        }
    }

    public boolean move(String displayName, int x, int y) {
        if (!ships.containsKey(displayName)) {
            var newShip = new Ship(x, y, displayName);
            newShip.setLastMoveTime(LocalDateTime.now());
            ships.put(displayName, newShip);
            return true;
        } else {
            var ship = ships.get(displayName);
            return ship.moveTo(x, y);
        }
    }

    public boolean shoot(String displayName, int x, int y) {
        var attackingShip = ships.get(displayName);
        if (attackingShip == null) {
            return false;
        }
        if (!attackingShip.isShootingAllowedBasedOnTime()) {
            return false;
        }

        // if hit self return false
        if (attackingShip.compareXy(x, y)) {
            return false;
        }

        var hitCount = ships.values().stream().filter(s -> s.compareXy(x, y)).count();

        if (hitCount > 0) {
            attackingShip.incrementHitCount();
        }
        return hitCount > 0;
    }

    public List<Ship> getShips() {
        return ships.values().stream()
                .sorted(Comparator.comparing(Ship::getDisplayName))
                .toList();
    }

    public Ship getShip(String displayName) {
        return ships.get(displayName);
    }
}
