package dk.mjolner.workshop.docker.parameters;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import static j2html.TagCreator.*;

@RestController
public class TaskCController {

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
                        Utils.bullet("Now we will play 'The Radar Game' together. The endpoint '/exercise' is called whenever this page is loaded/refreshed. The endpoint is not implemented correctly so this is up to you - you can find the code in the class called 'Exercise'. Instead of calling localhost:8080, you should call a remote url which is also called by your colleagues (This should be given to you by the instructur). Now you need to get as many points as possible (remember to put in your name)", isExerciseEndpointImplemented())
                ),
                div(a("Go back").withHref("/")),
                div(a("Kill the application").withHref("/kill"))
        );
    }

    public boolean isRunningOnPort8080(HttpServletRequest req){
        return req.getServerPort() == 8080;
    }

    public boolean isExerciseEndpointImplemented(){
        var restTemplate = new RestTemplate();
        try {
            var result = restTemplate.getForObject("http://localhost:8080/exercise", String.class);
            return true;
        } catch(Exception e) {
            System.out.println("Failed to call the exercise endpoint" + e.getMessage());
            return false;
        }
    
    }
}
