package dk.mjolner.workshop.docker.parameters;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import static j2html.TagCreator.*;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() throws IOException {
        return Utils.page(
                style("font-family: 'Roboto', sans-serif;"),
                h1("Good job!"),
                h3("You managed to run the docker container!"),
                h3("The exercises are divided in three parts"),
                div(a("1. Exercises with 'docker run'").withHref("./a")),
                div(a("2. Exercises with 'docker build' and 'docker-compose'").withHref("./b")),
                div(a("3. VS code - Remote Docker").withHref("./c"))
        );
    }

    @GetMapping(value = "/kill")
    public RedirectView killApplication(HttpServletRequest req) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(1);
            }
        }, 1000);

        return new RedirectView("/");
    }
}
