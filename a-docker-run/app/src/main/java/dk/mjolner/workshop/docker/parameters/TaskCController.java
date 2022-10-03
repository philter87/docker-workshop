package dk.mjolner.workshop.docker.parameters;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static j2html.TagCreator.*;

@RestController
public class TaskCController {

    @GetMapping(value = "c", produces = MediaType.TEXT_HTML_VALUE)
    public String home() throws IOException {
        return Utils.page(
                style("font-family: 'Roboto', sans-serif;"),
                h1("3. Exercises: vs code - remote docker"),
                h3("This exercise is quite different from the other exercises"),
                ul(
//                                bullet("Limit the amount of memory to 1000m (1000 Mb) used for the container ", isMemory1000MbOrBelow()),
                ),
                a("Kill the application").withHref("/kill")
        );
    }
}
