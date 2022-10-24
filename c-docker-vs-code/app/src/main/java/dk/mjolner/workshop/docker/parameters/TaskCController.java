package dk.mjolner.workshop.docker.parameters;

import dk.mjolner.workshop.docker.parameters.game.Ship;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import static dk.mjolner.workshop.docker.parameters.Exercise.BaseUrl;
import static dk.mjolner.workshop.docker.parameters.Exercise.UserDisplayName;
import static j2html.TagCreator.*;

@RestController
public class TaskCController {
    private final RestTemplate restTemplate = new RestTemplate();
    public static final String RemoteUrl = "http://20.223.122.66:8080";

    @GetMapping(value = "c", produces = MediaType.TEXT_HTML_VALUE)
    public String cPage(HttpServletRequest req) throws IOException {
        System.out.println("RemoteAdd:" + req.getRemoteAddr());
        System.out.println("LocalAdd:" + req.getLocalAddr());
        System.out.println("Server:" + req.getServerName() + ":" + req.getServerPort());
        return Utils.page(
                style("font-family: 'Roboto', sans-serif;"),
                h1("3. Exercises: VS code & 'Dev Containers'"),
                h3("This exercise is quite different from the other exercises. We want to play 'The Radar Game' by doing a bit of coding inside a docker container."),
                h3("The application showing you this page is a Java application (Java 17, gradle, spring boot). The purpose of this exercise is to change the code of this java application which you can find in the folder 'c-docker-vs-code/app'"),
                h3("The interesting part is that you need to do the development inside a docker container. Follow these steps:"),
                ul(
                        Utils.bullet("You need to install VS Code"),
                        Utils.bullet("You need to install the two extensions 'Docker' and 'Dev Containers' (recently, the name was 'remote container')"),
                        Utils.bullet("Open VS code, press 'F1' and then select 'Dev Container: Open folder in container ' (or Remote Container: Open folder in container). Select the folder: 'c-docker-vs-code/app'. Now you need to select several options: 'From a predefined container configuration', 'Java', '17-bullseye', 'node lts', 'Install Gradle', '0 selected'. Now you need to wait while for dev container to start up. It will also ask you to install an extension and you should accept this - it is only installed within the container."),
                        Utils.bullet("While you wait, you can take a look at the folder called '.devcontainer' which was created by VS code. It contains a Dockerfile and a devcontainer.json file. See if you can figure out what it means"),
                        Utils.bullet("Go to the class 'DockerWorkshopApplication' and see if you can run the application. The port is automatically published to 8080, so you can view result here: 'http://localhost:8080'. (If you have issues running java it might help to press F1 --> 'Java: Clean Java Language Server Workspace')", isRunningOnPort8080(req)),
                        Utils.bullet("Open the class called 'Exercise', where you will find the 'exercise()'-method. This is executed whenever you call 'http://localhost:8080/exercise' (or when this page is refreshed). The method should return 'true' instead of 'false'", isExerciseEndpointImplemented()),
                        Utils.bullet("Now you should try to make your first 'move' by modifying the 'exercise'-method again. You should uncomment the line after 'MOVEMENT OF SHIP' and change the 'UserDisplayName' to your actual name. When then endpoint 'localhost:8080/exercise' is called, it will also call 'localhost:8080/game/move' which will add you to the game. It will make this turn green, and you can also see your score if you visit http://localhost:8080/game/score.", isPlayerAddedToTheGame()),
                        Utils.bullet("We have played 'The Radar Game' locally in the previous steps. Now you can play against your colleagues if you change the BaseUrl to '" + RemoteUrl + "' in the class 'Exercise'. Rerun the application and refresh this page.", isPlayerAddedInRemoteGame()),
                        Utils.bullet("To win the game you need to change the algorithm in the 'exercise()'-method. You should automatically move every second and shoot every 5 seconds.")
                ),
                div(a("Go back").withHref("/")),
                div(a("Kill the application").withHref("/kill"))
        );
    }

    public boolean isRunningOnPort8080(HttpServletRequest req){
        return req.getServerPort() == 8080;
    }

    public boolean isExerciseEndpointImplemented(){
        return restTemplate.getForObject("http://localhost:8080/exercise", Boolean.class);
    }

    public boolean isPlayerAddedInRemoteGame(){
        var ships = restTemplate.getForObject(RemoteUrl + "/game/score", Ship[].class);
        for (Ship ship : ships) {
            if(ship.getDisplayName().equals(UserDisplayName)){
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerAddedToTheGame(){
        if(BaseUrl.equals(RemoteUrl)) {
            return true;
        }

        var ships = restTemplate.getForObject(BaseUrl + "/game/score", Ship[].class);
        for (Ship ship : ships) {
            if(!ship.getDisplayName().equals("YourName") && !ship.getDisplayName().equals("AI")){
                return true;
            }
        }
        return false;
    }
}
