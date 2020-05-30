package io.vertx.pagseguro.circuit.breaker;

import static io.vertx.pagseguro.utils.VertxRunner.runVerticle;

import io.vertx.core.DeploymentOptions;

/**
 * 
 * @author dbatista
 *
 */
public class Main {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		runVerticle(vertx -> {
			
			//vertx.deployVerticle(ServerVerticle.class.getName(), new DeploymentOptions());
			
			vertx.deployVerticle(ClientVerticle.class.getName(), new DeploymentOptions());
			
		});

	}

	

}
