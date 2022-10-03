package dk.mjolner.workshop.docker.parameters;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import static j2html.TagCreator.*;
import static j2html.TagCreator.a;

@RestController
public class TaskBController {
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping(value = "b", produces = MediaType.TEXT_HTML_VALUE)
    public String home() throws IOException {
        return Utils.page(
                style("font-family: 'Roboto', sans-serif;"),
                h1("2. Exercises: 'docker-build' and 'docker-compose'"),
                h3("A 'modern' webpage usually require three things: a database, a backend and a frontend."),
                h3("In this exercise, we need to create a webpage based on three images/containers and they need to communicate together."),
                h3("Lets say that this container (docker-workshop) is the backend (yes, a very visual backend). Now we need to 'connect' to a database and a frontend."),
                ul(
                        Utils.bullet("The database: Find the official postgres image in DockerHub and try to follow the instructions. The backend expects to find a postgres database running on port 9501 (default port is 5432) with the password 'DockerWorkshop'", isDatabaseRunning()),
                        Utils.bullet("The frontend: The frontend required a bit more work. We first need to build the image with a Dockerfile. The images should contain Nginx and some static html files. You can find the static files in the repository and a empty Dockerfile with some instructions on how to create a image with Nginx and the static files. The backend expects to see a frontend running on port 9502 (the default nginx is 80).", isFrontEndRunning()),
                        Utils.bullet("Connecting everything together: Everything is actually connected together now, but it seems a bit complicated to execute several 'docker run' commands to debug the application locally. This is where 'docker-compose' becomes relevant. It allows you to run all the containers with a single command ('docker-compose up') and a bit of configuration. The configuration is contained in a file called docker-compose.yml - there is an empty one in the git repository. You need to finish the docker-compose.yml file - there is a great example on the postgres DockerHub page: https://hub.docker.com/_/postgres. (You should use the service names: frontend, backend and db for this to turn green)", isDockerComposeRunning())
                ),
                div(a("Go back").withHref("/")),
                div(a("Kill the application").withHref("/kill"))
        );
    }

    private boolean isFrontEndRunning() {
        return isEndpointWorking("http://localhost:9502") || isEndpointWorking("http://frontend:80");
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
        return isDatabaseRunningAt("localhost:9501") || isDatabaseRunningAt("db:5432");
    }

    private boolean isDatabaseRunningAt(String url){
        try {
            var conn = DriverManager.getConnection("jdbc:postgresql://" + url + "/postgres?user=postgres&password=DockerWorkshop");
            var start = System.currentTimeMillis();
            var isValid = conn.isValid(200);;
            System.out.println("Time: " + (System.currentTimeMillis() - start));
            return isValid;

        } catch (SQLException e) {
            System.out.println("Unable to connect to postgres database with message " + e.getMessage());
            return false;
        }
    }
}
