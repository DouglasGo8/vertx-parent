package io.vertx.pagseguro.camel.bridge.feed;

import static io.vertx.camel.InboundMapping.fromCamel;
import static java.lang.System.out;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;

import io.vertx.camel.CamelBridge;
import io.vertx.camel.CamelBridgeOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.pagseguro.utils.VertxRunner;

/**
 *
 */
public class FeedMainVerticle extends AbstractVerticle {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		VertxRunner.runVerticle(vertx -> vertx.deployVerticle(FeedMainVerticle.class.getName(), new DeploymentOptions()));
	}

	/**
	 * @throws Exception
	 */
	@Override
	public void start() throws Exception {

		/**
		 * 
		 * Event bus Vertx Pipeline
		 */
		super.vertx.eventBus().consumer("announce", message -> out.println("ANNOUNCE >> " + message.body()));

		super.vertx.eventBus().consumer("errors", message -> out.println("ERROR >> " + message.body()));

		final CamelContext context = new DefaultCamelContext(new SimpleRegistry() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("filterService", new ReleasePostFilter());
			}
		});

		context.addRoutes(rssRouteBuilder());

		/**
		 * Mandatory
		 */
		context.start();

		/**
		 *
		 */
		CamelBridge.create(super.vertx, new CamelBridgeOptions(context)
					.addInboundMapping(fromCamel("seda:announce").toVertx("announce"))
					.addInboundMapping(fromCamel("seda:error").toVertx("errors"))).start();
	}

	/**
	 * @return
	 */
	private RouteBuilder rssRouteBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {

				// final String VERTX_BLOG_ATOM = "http://vertx.io/feed.xml";

				/**
				 * Error handle pipe
				 */
				errorHandler(deadLetterChannel("seda:errors"));

				// 403
				// from("rss:" + VERTX_BLOG_ATOM + "?splitEntries=true&consumer.delay=12s")
				// .to("seda:feeds");

				from("timer://myTimer?fixedRate=true&period=10s")
					.setBody(simple("Ping"))
					.to("seda:feeds");

				from("seda:feeds")
					.transform(simple("${body} Pong"))
					.to("seda:announce");

			}
		};
	}

}
