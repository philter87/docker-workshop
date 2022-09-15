package dk.mjolner.workshop.docker.parameters;

import j2html.tags.specialized.LiTag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static j2html.TagCreator.*;

@SpringBootApplication
@RestController
public class ParametersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParametersApplication.class, args);
	}

	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String home(){


		return html(
				head(
						title("Docker Parameters")
				),
				body(
						h1("Good job!"),
						h3("You managed to run the docker container. Now you should run the docker container with some other arguments. The bullet points below will turn green when the correct parameters are used"),
						ul(
								bullet("Limit the amount of memory to 300m (300 Mb) used for the container ", isMemory300MbOrBelow()),
								bullet("Limit to one cpu for the container. Current count: " + Runtime.getRuntime().availableProcessors(), isOneCpuCoreUsed()),
								li(),
								li(),
								li(),
								li()
						)
				)
		).render();
	}

	private boolean isOneCpuCoreUsed() {
		return Runtime.getRuntime().availableProcessors() == 1;
	}

	private static boolean isMemory300MbOrBelow() {
		return Runtime.getRuntime().maxMemory() <= 350_000_000;
	}


	public LiTag bullet(String text, boolean isSuccessful){
		var color = isSuccessful ? "background-color: #ABEBC6" : "background-color: #F5B7B1";
		return li(h3(text).withStyle(color));
	}


}
