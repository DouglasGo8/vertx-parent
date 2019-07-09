package io.vertx.pagseguro;

import static io.vertx.pagseguro.VertxRunner.runVerticle;

import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

/**
 * 
 * @author dbatista
 *
 */
public class MainVerticle extends AbstractVerticle {

	public static void main(String[] args) {

		runVerticle(vertx -> {
			DeploymentOptions deploymentOptions = new DeploymentOptions();

			vertx.deployVerticle(MainVerticle.class.getName(), deploymentOptions);
		});
	}

	/**
	 * 
	 */
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

		// Send Verticle

		LoggerFactory.getLogger(MainVerticle.class).info("MainVerticle ready...");
		
		super.vertx.deployVerticle(SendVerticle.class.getName(), new DeploymentOptions());
		super.vertx.deployVerticle(ReceiveVerticle.class.getName(), new DeploymentOptions());

	}

}
