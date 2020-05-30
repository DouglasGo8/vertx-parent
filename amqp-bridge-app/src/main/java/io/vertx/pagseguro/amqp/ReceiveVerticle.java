package io.vertx.pagseguro.amqp;

import static java.lang.System.out;

import org.slf4j.LoggerFactory;

import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.amqpbridge.AmqpConstants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import lombok.NoArgsConstructor;

/**
 * 
 * @author dbatista
 *
 */
@NoArgsConstructor
public class ReceiveVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

		LoggerFactory.getLogger(MainVerticle.class).info("ReceiveVerticle ready...");

		AmqpBridge bridgeAMQP = AmqpBridge.create(super.vertx);

		/**
		 * Works only ActiveMQ
		 */
		bridgeAMQP.start("localhost", 5672, res -> {
			if (!res.succeeded()) {
				out.println("Bridge startup failed: " + res.cause());
				return;
			}

			final MessageConsumer<JsonObject> consumer = bridgeAMQP.createConsumer("myAmqpAddress");

			consumer.handler(vertxMsg -> {
				JsonObject amqpMsgPayload = vertxMsg.body();
				Object amqpBody = amqpMsgPayload.getValue(AmqpConstants.BODY);
				out.println("Received a message with body: " + amqpBody);
			});

		});

	}
}
