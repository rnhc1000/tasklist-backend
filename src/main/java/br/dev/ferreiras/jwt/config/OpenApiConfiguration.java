package br.dev.ferreiras.jwt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
      Server server = new Server();
      server.setUrl("http://localhost:8080/apidocs");
      server.setDescription("Development");

      Contact myContact = new Contact();
      myContact.setName("Ricardo Ferreira");
      myContact.setEmail("ricardo@ferreiras.dev.br");

      Info information = new Info()
              .title("Daily Task App API")
              .version("1.0")
              .description("This API exposes endpoints to manage a daily task list.")
              .contact(myContact);
      return new OpenAPI().info(information).servers(List.of(server));
    }
}
