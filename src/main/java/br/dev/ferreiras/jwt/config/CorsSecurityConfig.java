package br.dev.ferreiras.jwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsSecurityConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry corsRegistry) {
    corsRegistry.addMapping(("/**"))
            .allowedOrigins("http://localhost:8080/")
            .allowedOrigins("http://127.0.0.1:8080/")
            .allowedOrigins("http://172.21.0.1:8080/")
            .allowedOrigins("http://192.168.15.11:8080/")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders(("*"));
  }
}
