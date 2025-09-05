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

import api.dtos.UserDto;


@Configuration
@EnableWebFluxSecurity
public class ApiGatewayAuthentication {
	
	@Bean
	SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
		http.
		csrf(csrf -> csrf.disable())
		.authorizeExchange(exchange -> exchange.pathMatchers("/currency-exchange").permitAll()
				.pathMatchers("/currency-conversion").hasRole("USER")
				.pathMatchers("/crypto-conversion/**").hasRole("USER")
				.pathMatchers("/currency-conversion-feign").hasRole("USER")
				.pathMatchers("/trade-service").hasRole("USER")
				.pathMatchers("/bank-account").hasAnyRole("ADMIN", "USER")
				.pathMatchers("/crypto-wallet").hasAnyRole("ADMIN", "USER")
				.pathMatchers("/crypto-exchange").permitAll()
				.pathMatchers("/users/**").hasAnyRole("ADMIN", "OWNER")
				.pathMatchers(HttpMethod.POST, "/bank-account/**", "/crypto-wallet/**").hasRole("ADMIN")
				.pathMatchers(HttpMethod.PUT, "/bank-account/**", "/crypto-wallet/**").hasRole("ADMIN")
				.pathMatchers(HttpMethod.DELETE, "/bank-account/**", "/crypto-wallet/**").hasRole("ADMIN")
				.pathMatchers(HttpMethod.DELETE, "/users/**").hasRole("OWNER")
				.pathMatchers(HttpMethod.POST, "/users/**").hasAnyRole("OWNER", "ADMIN")
				.pathMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("OWNER", "ADMIN"))
				.httpBasic(Customizer.withDefaults());
		
		return http.build();
	}
	
	@Bean
	MapReactiveUserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {
		
		// Obratiti paznju na URL prilikom dokerizacije
		
		ResponseEntity<List<UserDto>> response = new RestTemplate()
				.exchange("http://localhost:8770/users", HttpMethod.GET, null, // zbog dockera se menja
						new ParameterizedTypeReference<List<UserDto>> () {}); 
		
		List<UserDetails> users = new ArrayList<UserDetails>();
		for(UserDto dto : response.getBody()) {
			users.add(
					User.withUsername(dto.getEmail())
					.password(encoder.encode(dto.getPassword()))
					.roles(dto.getRole())
					.build()
					);
		}
		
		return new MapReactiveUserDetailsService(users);
				
	}
	
	@Bean
	BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

}
