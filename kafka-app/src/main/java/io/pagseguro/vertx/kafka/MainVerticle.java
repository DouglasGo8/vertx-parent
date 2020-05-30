package io.pagseguro.vertx.kafka;

import static io.vertx.pagseguro.utils.VertxRunner.runVerticle;

import java.io.File;
import java.util.Map;

import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.log4j.Logger;

import io.debezium.kafka.KafkaCluster;
import io.debezium.util.Testing;
import io.pagseguro.vertx.kafka.service.DashboardVerticle;
import io.pagseguro.vertx.kafka.service.MetricsVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

/**
 * 
 * @author dbatista
 *
 */
public class MainVerticle extends AbstractVerticle {

	/**
	 * 
	 */
	private KafkaCluster kafkaCluster;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		/**
		 * 
		 */
		runVerticle(vertx -> {

			final DeploymentOptions deploymentOptions = new DeploymentOptions();

			deploymentOptions.setInstances(1);

			vertx.deployVerticle(MainVerticle.class.getName(), deploymentOptions);

		});

	}

	@Override
	public void start() throws Exception {
		
		Logger.getLogger(MainVerticle.class).info("MainVerticle ready to start....");
		//
		final File dataDir = Testing.Files.createTestingDirectory("cluster");
		dataDir.deleteOnExit();
		
		/**
		 * Simulates a Kafka Cluster on Local station
		 */
    	kafkaCluster = new KafkaCluster()
      		.usingDirectory(dataDir)
      		.withPorts(2181, 9092)
      		.addBrokers(1)
      		.deleteDataPriorToStartup(true)
			  .startup();
    	/**
    	 * Consumer
    	 */
    	@SuppressWarnings({ "unchecked", "rawtypes" })
    	final JsonObject consumerConfig = new JsonObject((Map) kafkaCluster.useTo()
    		      .getConsumerProperties("the_group", "the_client", OffsetResetStrategy.LATEST));
    	super.vertx.deployVerticle(DashboardVerticle.class.getName(),
        		new DeploymentOptions().setConfig(consumerConfig).setInstances(1));
    	
    	/**
    	 * Producer
    	 */
        @SuppressWarnings({ "unchecked", "rawtypes" })
		final JsonObject producerConfig = new JsonObject((Map) kafkaCluster.useTo()
        	      .getProducerProperties("the_producer")); 
        super.vertx.deployVerticle(MetricsVerticle.class.getName(),
        		new DeploymentOptions().setConfig(producerConfig).setInstances(3));
		
	}

	@Override
	public void stop() throws Exception {
		this.kafkaCluster.shutdown();
	}

}
