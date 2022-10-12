package dk.mjolner.workshop.docker.parameters.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {
    private ShipService shipService;

    @Autowired
    public GameController(ShipService shipService) {
        this.shipService = shipService;
    }

    @CrossOrigin
    @GetMapping("game/move")
    public String move(@RequestParam("x") int x, @RequestParam("y") int y, @RequestParam("displayName") String displayName) {
        var isMoved = shipService.move(displayName, x, y);
        return isMoved ? "moved" : "did not move";
    }

    @CrossOrigin
    @GetMapping("game/shoot")
    public String shoot(@RequestParam("x") int x, @RequestParam("y") int y, @RequestParam("displayName") String displayName) {
        var isHit = shipService.shoot(displayName, x, y);
        return isHit ? "hit" : "did not hit";
    }

    @CrossOrigin
    @GetMapping("game/score")
    public List<Ship> ships() {
        return this.shipService.getShips();
    }

    @CrossOrigin
    @GetMapping("game/reset")
    public void resetGame() {
        this.shipService.reset();
    }

    @CrossOrigin
    @GetMapping("game/winner")
    public String drawWinner() {
        var ships = this.shipService.getShips();
        var totalPoints = ships.stream().mapToDouble(Ship::getPoints).sum();
        var winnerPoint = Math.random() * totalPoints;
        System.out.println("Winner point: " + winnerPoint + ", total:" + totalPoints);
        var currentPoint = 0L;
        for (Ship ship : ships) {
            var newPoints = currentPoint + ship.getPoints();
            System.out.println("Points: " + ship.getDisplayName() + ", " + ship.getPoints() + ": total: " + newPoints);
            if (winnerPoint < newPoints) {
                return ship.getDisplayName();
            }
            currentPoint = newPoints;
        }
        return "none";
    }
}
