package io.vertx.pagseguro.rmi;

import static io.vertx.camel.OutboundMapping.fromVertx;

import org.apache.camel.CamelContext;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.vertx.camel.CamelBridge;
import io.vertx.camel.CamelBridgeOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.pagseguro.VertxRunner;

/**
 * 
 * @author dbatista
 *
 */
public class RmiMainVerticle extends AbstractVerticle {

	public static void main(String[] args) {

		VertxRunner.runVerticle(vertx -> {
			vertx.deployVerticle(RmiMainVerticle.class.getName(), new DeploymentOptions());
		});
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

		LoggerFactory.getLogger(RmiMainVerticle.class).info("Verticle ready...");

		@SuppressWarnings("resource")
		ApplicationContext appContext = new ClassPathXmlApplicationContext("META-INF/spring/app-context.xml");

		CamelContext camel = appContext.getBean("camel", CamelContext.class);

		CamelBridge.create(super.vertx, new CamelBridgeOptions(camel)
						.addOutboundMapping( /* from Vertx Bus */ fromVertx("invocation").toCamel("rmiService")))
				.start();

		vertx.createHttpServer().requestHandler(request -> {

			final String param = request.getParam("name"); // ?name=Value

			if (null == param)
				throw new RuntimeException("Missing param [name]");

			super.vertx.eventBus().<String>send("invocation", param, reply -> {
				if (reply.failed()) {
					request.response().setStatusCode(503).end(reply.cause().getMessage());
				} else {
					request.response().setStatusCode(200).end(reply.result().body());
				}
			});

		}).listen(8777);

	}

}
