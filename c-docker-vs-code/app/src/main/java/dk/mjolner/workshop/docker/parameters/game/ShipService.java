package dk.mjolner.workshop.docker.parameters.game;

import java.time.LocalDateTime;
import java.util.*;

public class ShipService {
    private Map<String, Ship> ships;
    private Timer aiTimer = new Timer();

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
        if (ships.size() == 0) {
            createAiShip();
        }

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

    private void createAiShip() {
        ships.put("AI", new Ship(5, 5, "AI"));
        aiTimer.cancel();
        aiTimer = new Timer();

        aiTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                var aiShip = ships.get("AI");
                System.out.println("Move ai ship");
                if(aiShip != null){
                    var x = aiShip.getX() + (int) Math.round( 2 * Math.random() - 1);
                    var y = aiShip.getY() + (int) Math.round( 2 * Math.random() - 1);
                    aiShip.moveTo(x, y);
                }
            }
        }, 0, 5000);
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

    public void reset() {
        this.ships = new HashMap<>();
    }
}
