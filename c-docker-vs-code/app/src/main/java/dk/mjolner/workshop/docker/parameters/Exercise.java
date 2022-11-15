package dk.mjolner.workshop.docker.parameters;

import dk.mjolner.workshop.docker.parameters.game.Ship;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Exercise {
    private final RestTemplate restTemplate = new RestTemplate();
    public static final String BaseUrl = "http://localhost:8080";
    public static final String UserDisplayName = "YourName";

    @GetMapping(value = "exercise")
    public boolean exercise() {
        var x = 5;
        var y = 5;

        // MOVEMENT OF SHIP. Allowed once every second. Gives 1 point
        // restTemplate.getForObject(BaseUrl + "/game/move?x=" + x + "&y=" + y + "&displayName=" + UserDisplayName, String.class);

        // SHOOTING WITH SHIP. Allowed once every five second. Gives 5 points.
        // restTemplate.getForObject(BaseUrl + "/game/shoot?x=" + x + "&y=" + y + "&displayName=" + UserDisplayName, String.class);

        // GETTING SHIP COORDINATES
        // var ships = restTemplate.getForObject(BaseUrl + "/game/score", Ship[].class);

        return false;
    }

}
