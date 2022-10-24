package dk.mjolner.workshop.docker.parameters;

import dk.mjolner.workshop.docker.parameters.game.Ship;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Exercise {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping(value = "exercise")
    public boolean exercise() {
        var baseUrl = "http://localhost:8080";
        var displayName = "YourName";

        // Get the ships
        var ships = restTemplate.getForObject(baseUrl + "/game/score", Ship[].class);

        var x = 5;
        var y = 5;

        // ** MOVING YOUR SHIP **
        // You can move the location of your ship. Every move gives you 1 point. You can only move to locations next to your current location.
        // You may need to find your ship in the ships-array to know your current location. You can only move once every second.
        restTemplate.getForObject(baseUrl + "/game/move?x=" + x + "&y=" + y + "&displayName=" + displayName, String.class);

        // ** SHOOTING WITH YOUR SHIP **
        // You can shoot other ships in the game. You get 5 points for hitting an opponent, but you can only do this every 5 seconds. 
        // There are no penalties for missing or shooting yourself. You get 5 points per hit.
        // 
        // restTemplate.getForObject(baseUrl + "/game/shoot?x=" + x + "&y=" + y + "&displayName=" + displayName, String.class);

        return false;
    }

}
