package io.vertx.pagseguro.cassandra;

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

		VertxRunner.runVerticle(runner -> {
			runner.deployVerticle(MainVerticle.class.getName(), new DeploymentOptions());
		});

	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

		/**
		 * 1 - SimpleStatementVerticle
		 */
		VertxRunner.runVerticle(runner -> {
			runner.deployVerticle(SimpleStatementVerticle.class.getName(), new DeploymentOptions());
		});

		/**
		 * 2 - PreparedStatementVerticle
		 */
		VertxRunner.runVerticle(runner -> {
			runner.deployVerticle(PreparedStatementVerticle.class.getName(), new DeploymentOptions());
		});

		/**
		 * 3 - StreamingStatementVerticle
		 */
		VertxRunner.runVerticle(runner -> {
			runner.deployVerticle(StreamingStatementVerticle.class.getName(), new DeploymentOptions());
		});

	}

}
