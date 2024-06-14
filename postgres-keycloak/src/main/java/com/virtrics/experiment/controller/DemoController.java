package com.virtrics.experiment.controller;

import com.virtrics.experiment.model.LoginDto;
import com.virtrics.experiment.util.JwtUtil;
import io.prometheus.client.spring.web.PrometheusTimeMethod;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@Slf4j
public class DemoController {

    @GetMapping("/User")
    @PreAuthorize("hasRole('Virtrics-User')")
    @PrometheusTimeMethod(name = "DemoController_helloRestaurantCustomer(jwt)_duration_seconds",
            help =
                    "Some helpful info here")
    public String helloRestaurantCustomer(@RequestHeader(name = "Authentication") String bearerToken) {
        log.info("> helloRestaurantCustomer > {} ", bearerToken);
        return "Hello RestaurantCustomer " + bearerToken;
    }

    @GetMapping("/Administrator")
    @PreAuthorize("hasRole('Virtrics-Administrator')")

    @PrometheusTimeMethod(name = "DemoController_helloAdministrator(jwt)_duration_seconds", help =
            "Some helpful info here")
    public String helloAdministrator(@RequestHeader(name = "Authentication") String bearerToken) {
        log.info("> helloAdministrator > {} ", bearerToken);
        return "Hello Administrator" + bearerToken;
    }


    @GetMapping("/login")
    @PrometheusTimeMethod(name = "DemoController_login()_duration_seconds", help = "Some " +
            "helpful info here")
    public String login(LoginDto loginDto) {
        log.info("> login > {} ", loginDto);
        return JwtUtil.generateJwtToken(loginDto.username(), loginDto.password());
    }

    @GetMapping("/generateJwtToken")
    @PrometheusTimeMethod(name = "DemoController_generateJwtToken()_duration_seconds", help =
            "Some " +
                    "helpful info here")
    public String generateJwtToken(@RequestBody(required = true) LoginDto loginDto) {
        log.info("> generateJwtToken > {} ", loginDto);
        return JwtUtil.generateJwtToken(loginDto.username(), loginDto.password());
    }

}
