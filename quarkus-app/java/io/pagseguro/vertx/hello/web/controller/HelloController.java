package io.pagseguro.vertx.hello.web.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author dbatista
 *
 */
@Data
@Path("/")
@NoArgsConstructor
public class HelloController {

	/**
	 * 
	 * @param asyncResponse
	 * @param vertx
	 * @param id
	 */
	@GET
	@Path("/products/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public void sayHello
	(
			@Suspended final AsyncResponse asyncResponse, 
			@Context Vertx vertx,
			@PathParam("id") String id
	) {
		
		
		if (null == id) {
			asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
			return;
		}
		
		final JsonObject body = new JsonObject().put("op", "get").put("id", id);
		/**
		 * 
		 */
		vertx.eventBus().<JsonObject>send("backend", body, msg -> {

			if (msg.succeeded()) {
				JsonObject json = msg.result().body();
				if (json != null) {
					asyncResponse.resume(json.encode());
				} else {
					asyncResponse.resume(Response.status(Response.Status.NOT_FOUND).build());
				}
			} else {
				asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
			}
		});
		
	}
}
