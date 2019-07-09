package io.vertx.pagseguro;

import static java.lang.System.out;

import java.time.LocalTime;

import org.slf4j.LoggerFactory;

import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.amqpbridge.AmqpConstants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import lombok.NoArgsConstructor;

/**
 * 
 * @author dbatista
 *
 */
@NoArgsConstructor
public class SendVerticle extends AbstractVerticle {

	private int count = 1;

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub

		LoggerFactory.getLogger(MainVerticle.class).info("SendVerticle ready...");

		AmqpBridge bridgeAMQP = AmqpBridge.create(super.vertx);

		/**
		 * Works only Apache ActiveMQ
		 */
		bridgeAMQP.start("localhost", 5672, res -> {
			
			if (!res.succeeded()) {
				out.println("Bridge startup failed: " + res.cause());
				return;
			}

			final MessageProducer<JsonObject> producer = bridgeAMQP.createProducer("myAmqpAddress");
			
			out.println("Producer created, scheduling sends.");

			super.vertx.setPeriodic(5000, v -> {
				
				final JsonObject amqpMsgPayload = new JsonObject();

				amqpMsgPayload.put(AmqpConstants.BODY, "myStringContent" + count + " at " + LocalTime.now().toString());
				producer.send(amqpMsgPayload);

				out.println("Sent message: " + count++);
			});

		});

	}

}
