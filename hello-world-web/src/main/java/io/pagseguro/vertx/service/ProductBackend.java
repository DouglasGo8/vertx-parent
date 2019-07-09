package io.pagseguro.vertx.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

/**
 * 
 * @author dbatista
 *
 */

public class ProductBackend extends AbstractVerticle {

	private Map<String, JsonObject> products = new HashMap<>();

	/**
	 * 
	 */
	public ProductBackend() {

		// Setup initial data
		addProduct(new JsonObject().put("id", "prod3568").put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
		addProduct(new JsonObject().put("id", "prod7340").put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
		addProduct(new JsonObject().put("id", "prod8643").put("name", "Spatula").put("price", 1.00).put("weight", 80));
	}
	
	/**
	 * 
	 */
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		Logger.getLogger(Server.class).info("ProductBackend Verticle started...");
		
		super.vertx.eventBus().<JsonObject>consumer("backend", msg -> {

			JsonObject json = msg.body();

			switch (json.getString("op", "")) {
			case "get": {
				String id = json.getString("id");
				msg.reply(products.get(id));
				break;
			}
			default: {
				msg.fail(0, "operation not permitted");
			}
			}

		});
	}
	
	
	
	/**
	 * 
	 * @param product
	 * @return
	 */
	private boolean addProduct(JsonObject product) {
		if (product.containsKey("name") || product.containsKey("price") || product.containsKey("weight")) {
			products.put(product.getString("id"), product);
			return true;
		} else {
			return false;
		}
	}
	
}
