package apiGateway.routing;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class RoutingConfiguration {
	
	@Bean
	RouteLocator gatewayRouting(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p.path("/currency-exchange").uri("lb://currency-exchange"))
				.route(p -> p.path("/currency-conversion-feign").uri("lb://currency-conversion"))
				.route(p -> p.path("/currency-conversion").filters(f -> f.rewritePath("/currency-conversion", "/currency-conversion-feign"))
						.uri("lb://currency-conversion"))
				.route(p -> p.path("/users/**").uri("lb://users-service"))
				.route(p -> p.path("/bank-account/**").uri("lb://bank-account"))
				.route(p -> p.path("/crypto-exchange").uri("lb://crypto-exchange"))
				.route(p -> p.path("/crypto-wallet/**").uri("lb://crypto-wallet"))
				.route(p -> p.path("/crypto-conversion").uri("lb://crypto-conversion"))
				.route(p -> p.path("/trade-service/**").uri("lb://trade-service"))
				.build();
	}

}
