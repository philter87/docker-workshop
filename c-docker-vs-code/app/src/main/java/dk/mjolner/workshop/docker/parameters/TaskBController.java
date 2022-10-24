package dk.mjolner.workshop.docker.parameters;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

import static j2html.TagCreator.*;
import static j2html.TagCreator.a;

@RestController
public class TaskBController {
    private RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(100)).setReadTimeout(Duration.ofMillis(100)).build();

    @GetMapping(value = "b", produces = MediaType.TEXT_HTML_VALUE)
    public String bPage() throws IOException {
        return Utils.page(
                style("font-family: 'Roboto', sans-serif;"),
                h1("2. Exercises: 'docker-build' and 'docker-compose'"),
                h3("A 'modern' webpage usually require three things: a database, a backend and a frontend."),
                h3("In this exercise, we need to create a webpage based on three images/containers and they need to communicate together."),
                h3("It would be inconvenient, to use the 'docker run'-command three times to get everything up and running. Therefore we want to use 'docker-compose'. 'docker-compose' can run several containers with a single command (docker-compose up) and a docker-compose.yml file."),
                h3("Lets say that this container (docker-workshop) is the backend (yes, a very visual backend) and then we also need a database and a frontend. Lets start with the backend: "),
                ul(
                        Utils.bullet("The backend. The image 'philter87/docker-workshop' is the backend. We need to run this with docker-compose. Luckily, there is already a 'docker-compose.yml' file in the folder 'b-docker-build'. Go to the folder in a terminal and write 'docker-compose up'. Remember to go to localhost:9500", isBackendWorking()),
                        Utils.bullet("The database. Next you will need to add the database configuration to the docker-compose.yml file. Try and find the official postgres image on DockerHub. Somewhere on the page, there is a docker-compose example. Use that with some minor changes. The password should be 'DockerWorkshop' and the service should be called 'db'.", isDatabaseRunning()),
                        Utils.bullet("The frontend. The frontend also needs to be added to the docker-compose.yml file. The frontend container requires a bit more work, because we have no image and therefore we need to create an image with an Dockerfile. You can find an incomplete Dockerfile in the repository 'b-docker-build/Dockerfile' - the file contains some hints. The image should be based on the official dockerhub nginx-image and then we need to add (or COPY) some static html files into the image. The static files are located in the folder 'b-docker-build/frontend-radar/dist'. When the Dockerfile is complete, you can build the image with 'docker build --tag frontend-radar .', which creates a image called 'frontend-radar'. For this bullet point to turn green, the frontend must publish port 9502 (the default nginx is 80) and the service needs be called 'frontend'. Visit the page at 'http://localhost:9502' to see the result.", isFrontEndRunning())
                ),
                div(a("Go back").withHref("/")),
                div(a("Kill the application").withHref("/kill"))
        );
    }

    private boolean isBackendWorking(){
        return isEndpointWorking("http://backend:8080");
    }

    private boolean isFrontEndRunning() {
        return isEndpointWorking("http://frontend:80");
    }

    private boolean isDockerComposeRunning(){
        return isEndpointWorking("http://frontend:80") && isDatabaseRunningAt("db:5432");
    }

    private boolean isEndpointWorking(String url){
        try {
            var response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e){
            System.out.println("Unable to connect to frontend with message: " + e.getMessage());
            return false;
        }
    }

    private boolean isDatabaseRunning() {
        return isDatabaseRunningAt("db:5432");
    }

    private boolean isDatabaseRunningAt(String url){
        var start = System.currentTimeMillis();
        try {
            var conn = DriverManager.getConnection("jdbc:postgresql://" + url + "/postgres?user=postgres&password=DockerWorkshop");
            System.out.println("Time0: " + (System.currentTimeMillis() - start));
            var isValid = conn.isValid(100);
            System.out.println("Time: " + (System.currentTimeMillis() - start));
            return isValid;

        } catch (SQLException e) {
            System.out.println("Time: " + (System.currentTimeMillis() - start));
            System.out.println("Unable to connect to postgres database with message " + e.getMessage());
            return false;
        }
    }
}
