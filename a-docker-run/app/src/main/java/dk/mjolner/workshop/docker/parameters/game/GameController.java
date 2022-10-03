package dk.mjolner.workshop.docker.parameters.game;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("game/move")
    public String move(@RequestParam("x") int x, @RequestParam("y") int y, @RequestParam("displayName") String displayName) {
        var isMoved = shipService.move(displayName, x, y);
        return isMoved ? "moved" : "did not move";
    }

    @GetMapping("game/shoot")
    public String shoot(@RequestParam("x") int x, @RequestParam("y") int y, @RequestParam("displayName") String displayName) {
        var isHit = shipService.shoot(displayName, x, y);
        return isHit ? "hit" : "did not hit";
    }

    @GetMapping("game/score")
    public List<Ship> ships(){
        return this.shipService.getShips();
    }
}
