package apiGateway.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;

import api.dto.UserDto;

@Configuration
@EnableWebFluxSecurity
public class ApiGatewayAuthentication {

	
	@Bean
	SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeExchange(exchange -> exchange
				
				//Existing routes
				.pathMatchers("/currency-exchange").hasAnyRole("USER", "ADMIN", "OWNER")
	            .pathMatchers("/currency-conversion").hasRole("USER")
	            .pathMatchers(HttpMethod.GET, "/users").hasAnyRole("ADMIN", "OWNER")
	            .pathMatchers(HttpMethod.POST, "/users/newUser").hasAnyRole("ADMIN", "OWNER")
	            .pathMatchers(HttpMethod.PUT, "/users/updateUser/**").hasAnyRole("ADMIN", "OWNER")
	            .pathMatchers(HttpMethod.DELETE, "/users/deleteUser/**").hasAnyRole("ADMIN", "OWNER")
	            .pathMatchers(HttpMethod.POST, "/users/newAdmin").hasRole("OWNER")
	            .pathMatchers(HttpMethod.PUT, "/users/updateAdmin/**").hasRole("OWNER")
	            .pathMatchers(HttpMethod.DELETE, "/users/deleteAdmin/**").hasRole("OWNER")
	            .pathMatchers(HttpMethod.PUT, "/users/updateOwner/**").hasRole("OWNER")
				).httpBasic(Customizer.withDefaults());
		
		return http.build();
	}
	
	@Bean
	MapReactiveUserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {
		ResponseEntity<List<UserDto>> response =
				//Obratiti paznju prilikom rada sa Dockerom
				// Bez dockera localhost:8770/users
				// Za docker users-service:8770/users
				new RestTemplate().exchange("http://localhost:8770/users", HttpMethod.GET,
						null, new ParameterizedTypeReference<List<UserDto>>() {});
		List<UserDetails> users = new ArrayList<UserDetails>();
		for(UserDto user : response.getBody()) {
			users.add(
					User.withUsername(user.getEmail())
					.password(encoder.encode(user.getPassword()))
					.roles(user.getRole())
					.build());
				
		}
		
		return new MapReactiveUserDetailsService(users);
				
	}
	
	@Bean
	BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
}
