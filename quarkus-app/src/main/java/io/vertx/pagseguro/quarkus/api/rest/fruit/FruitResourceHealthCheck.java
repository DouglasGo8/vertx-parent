package io.vertx.pagseguro.quarkus.api.rest.fruit;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

/**
 * 
 * @author dbatista
 *
 */
@Health
@ApplicationScoped
@SuppressWarnings("deprecation")
public class FruitResourceHealthCheck implements HealthCheck {

	@Override
	public HealthCheckResponse call() {
		// TODO Auto-generated method stub
		return HealthCheckResponse
					.named("Health check with data")
					.up()
					.withData("Hello", "from Fruit Resource Service")
					.withData("At", LocalDateTime.now().toString())
					.build();
	}

}
