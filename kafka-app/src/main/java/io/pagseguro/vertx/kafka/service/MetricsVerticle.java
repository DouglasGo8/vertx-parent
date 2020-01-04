package io.pagseguro.vertx.kafka.service;

import java.lang.management.ManagementFactory;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import com.sun.management.OperatingSystemMXBean;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaWriteStream;
import lombok.NoArgsConstructor;

/**
 * 
 * @author dbatista
 *
 */
@NoArgsConstructor
@SuppressWarnings("restriction")
public class MetricsVerticle extends AbstractVerticle {

	/**
	 * 
	 */
	private KafkaWriteStream<String, JsonObject> producer;

	/**
	 * 
	 */
	private OperatingSystemMXBean systemMBean;

	/**
	 * 
	 */
	@Override
	public void start() throws Exception {

		Logger.getLogger(MetricsVerticle.class).info("MetricsVerticle ready...");

		final JsonObject config = super.config();

		final String pid = UUID.randomUUID().toString();

		this.systemMBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

		this.producer = KafkaWriteStream.<String, JsonObject>create(
				super.vertx, 
				config.getMap(), 
				String.class,
				JsonObject.class);

		super.vertx.setPeriodic(2000, id -> { // each 2 seconds creates a Message

			// Logger.getLogger(MetricsVerticle.class).info("Producing message...");

			JsonObject metrics = new JsonObject();

			metrics.put("CPU", systemMBean.getProcessCpuLoad());
			metrics.put("Mem", systemMBean.getTotalPhysicalMemorySize() - systemMBean.getFreePhysicalMemorySize());
			//
			producer.write(new ProducerRecord<String, JsonObject>(
					"the_topic",new JsonObject().put(pid, metrics)));

		});

	}

	/**
	 * 
	 */
	@Override
	public void stop() throws Exception {
		if (!(null == producer)) {
			producer.close();
		}
	}

}
