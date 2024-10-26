package com.munafsheikh.rbacs.rbacstest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@Configuration
@RestController
@Slf4j
public class RbacstestApplication {


	public static final String[] adminAccessAreas = {
			"/swagger-ui/**","/secure/**", "/actuator/**",
	};

	private List<UserDetails> userDb = List.of(
			User.withDefaultPasswordEncoder()
					.username("u")
					.password("u")
					.roles("USER")
					.build(),
			User.withDefaultPasswordEncoder()
					.username("r")
					.password("r")
					.roles("ASDASD")
					.build(),
			User.withDefaultPasswordEncoder()
					.username("a")
					.password("a")
					.roles("ADMIN")
					.build());



	@GetMapping("secure")
	public String getSecure(){
		return "secure";
	}


	enum Role{

		USER("USER"),ADMIN("ADMIN");
		String v;

        Role(String val) {
            this.v = val;
        }


	}

	@PostMapping("secure/add")
	public String createUser(String username, String password, Role role){
		userDb.add(User.withDefaultPasswordEncoder()
				.username(username)
				.password(password)
				.roles(role.v)
				.build());
		return "Created";
	}

	@GetMapping("secure/list")
	public List<UserDetails> listUsers(){
		return userDb;
	}

	@GetMapping("insecure")
	public String getInSecure(){
		return "insecure";
	}

	public static void main(String[] args) {
		log.debug("adminAccessAreas: {}",adminAccessAreas);
		SpringApplication.run(RbacstestApplication.class, args);
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager(userDb);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers(adminAccessAreas).hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.formLogin((form) -> form.permitAll())
				.logout((logout) -> logout.permitAll());

		return httpSecurity.build();
	}

}
