package dk.mjolner.workshop.docker.parameters;

import dk.mjolner.workshop.docker.parameters.game.ShipService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DockerWorkshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerWorkshopApplication.class, args);
    }


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ShipService getShipService(){
        return new ShipService();
    }
}
