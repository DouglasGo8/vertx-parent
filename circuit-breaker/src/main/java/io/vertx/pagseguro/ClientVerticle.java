package io.vertx.pagseguro;

import static java.lang.System.out;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import lombok.NoArgsConstructor;

/**
 * 
 * @author dbatista
 *
 */
@NoArgsConstructor
public class ClientVerticle extends AbstractVerticle {

	@Override
	@SuppressWarnings("deprecation")
	public void start() throws Exception {
		// TODO Auto-generated method stub

		CircuitBreakerOptions cbo = new CircuitBreakerOptions().setMaxFailures(5).setTimeout(5000)
				.setFallbackOnFailure(true);

		CircuitBreaker breaker = CircuitBreaker.create("my-circuite-breaker", vertx, cbo).openHandler(v -> {
			out.println("Circuit opened");
		}).closeHandler(v -> {
			out.println("Circuit closed");
		});

		Future<String> result = breaker.executeWithFallback(future -> {

			vertx.createHttpClient().getNow(8999, "localhost", "/", resp -> {
				if (resp.statusCode() != 200) {
					future.fail("HTTP Error");
				} else {
					resp.exceptionHandler(future::fail).bodyHandler(b -> {
						future.complete(b.toString());
					});
				}
			});
		}, v -> {
			return "Damn (fallback execution)";
		});

		result.setHandler(ar -> {
			out.println("Result:" + ar.result());
		});
	}
}
