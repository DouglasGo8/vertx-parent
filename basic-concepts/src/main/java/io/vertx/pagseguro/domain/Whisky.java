package io.vertx.pagseguro.domain;

import java.io.Serializable;

import io.vertx.core.json.JsonObject;
import lombok.Getter;

/**
 * 
 * @author dbatista
 *
 */
@Getter
public class Whisky implements Serializable {

	private String id;
	private String name;
	private String origin;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Whisky() {
		this.id = "";
	}

	/**
	 * 
	 * @param name
	 * @param origin
	 */
	public Whisky(String name, String origin) {
		this.name = name;
		this.origin = origin;
		this.id = "";
	}

	public Whisky(String id, String name, String origin) {
		this.id = id;
		this.name = name;
		this.origin = origin;
	}

	/**
	 * 
	 * @param json
	 */
	public Whisky(JsonObject json) {
		this.id = json.getString("_id");
		this.name = json.getString("name");
		this.origin = json.getString("origin");
	}

	public Whisky setName(String name) {
		this.name = name;
		return this;
	}

	public Whisky setOrigin(String origin) {
		this.origin = origin;
		return this;
	}

	public Whisky setId(String id) {
		this.id = id;
		return this;
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject().put("name", name).put("origin", origin);
		if (id != null && !id.isEmpty()) {
			json.put("_id", id);
		}
		return json;
	}

}
