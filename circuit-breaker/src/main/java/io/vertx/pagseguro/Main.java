package io.vertx.pagseguro;

import static io.vertx.pagseguro.VertxRunner.runVerticle;

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
