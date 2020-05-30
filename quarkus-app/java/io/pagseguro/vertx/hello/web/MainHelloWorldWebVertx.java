package io.pagseguro.vertx.hello.web;

import org.apache.log4j.Logger;

import io.pagseguro.vertx.hello.web.service.ProductBackend;
import io.pagseguro.vertx.hello.web.service.Server;
import io.vertx.core.AbstractVerticle;
import io.vertx.pagseguro.utils.VertxRunner;

/**
 * 
 */
public class MainHelloWorldWebVertx extends AbstractVerticle {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		VertxRunner.runVerticle(MainHelloWorldWebVertx.class);

	}

	/**
	 * 
	 */
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

		Logger.getLogger(MainHelloWorldWebVertx.class).info("Staring Main Vertx...");

		super.vertx.deployVerticle(Server.class.getName());
		super.vertx.deployVerticle(ProductBackend.class.getName());

	}

}
