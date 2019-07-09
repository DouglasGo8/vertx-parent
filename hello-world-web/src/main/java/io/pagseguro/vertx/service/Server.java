package io.pagseguro.vertx.service;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.server.vertx.VertxRequestHandler;
import org.jboss.resteasy.plugins.server.vertx.VertxResteasyDeployment;

import io.pagseguro.vertx.controller.HelloController;
import io.vertx.core.AbstractVerticle;
import lombok.NoArgsConstructor;

/**
 * 
 * @author dbatista
 *
 */
@NoArgsConstructor
public class Server extends AbstractVerticle {

	
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

		Logger.getLogger(Server.class).info("Server Verticle started...");

		VertxResteasyDeployment deployment = new VertxResteasyDeployment();

		deployment.start();
		deployment.getRegistry().addPerInstanceResource(HelloController.class);

		super.vertx.createHttpServer()
			//
			.requestHandler(new VertxRequestHandler(vertx, deployment))
			//
			.listen(8088,
				ar -> {
					Logger.getLogger(Server.class)
						.info("Server Http start on port " + ar.result().actualPort());
				});
	}

}
