package com.virtrics.experiment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.virtrics.experiment.config.BaseToSwaggerURIFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = {"com.virtrics.experiment"})
@EntityScan(basePackages = {"com.virtrics.experiment"})
@EnableJpaRepositories
@Profile("uat")
public class RestaurantVotingSystemApplication {


    @Autowired
    private ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(RestaurantVotingSystemApplication.class, args);
    }

    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        BaseToSwaggerURIFilter baseToSwaggerURIFilter = new BaseToSwaggerURIFilter();
        filterRegistrationBean.setFilter(baseToSwaggerURIFilter);
        return filterRegistrationBean;
    }

    //
    @Bean
    OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("RestaurantVotingSystem")
                        .version("3.0.0")
                        .description("API for managing RestaurantVotingSystem's menu and orders")
                        .contact(new Contact()
                                .name("Munaf Sheikh")
                                .email("munaf.sheikh@gmail.com")
                                .url("https://www.munafsheikh.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .addServersItem(new Server()
                        .url("http://localhost:8081/")
                        .description("Production server"))
                .addServersItem(new Server()
                        .url("https://api.rvs.com/v1")
                        .description("Production server"))
                .addServersItem(new Server()
                        .url("https://api-staging.rvs.com/v1")
                        .description("Staging server"))
               /* .security(List.of(new SecurityRequirement()
                        .name("bearerAuth")
                        .scopes(List.of("read:menu", "write:orders")))))
       .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))*/;
    }

}

