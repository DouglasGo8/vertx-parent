package io.vertx.pagseguro.amqp;

import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.pagseguro.utils.VertxRunner;

/**
 * 
 * @author dbatista
 *
 */
public class MainVerticle extends AbstractVerticle {

	public static void main(String[] args) {

		VertxRunner.runVerticle(vertx -> {
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
