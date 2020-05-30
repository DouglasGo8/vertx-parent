package io.vertx.pagseguro.quarkus.api.rest.vertx;


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 *
 */
@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class VertxJsonResource {

    @GET
    @Path("/{name}/object")
    public JsonObject jsonObject(@PathParam("name") String name) {
        return new JsonObject().put("Hi, Hello", name);
    }

    @GET
    @Path("/{name}/array")
    public JsonArray jsonArray(@PathParam("name") String name) {
        return new JsonArray().add("Hi, Hello").add(name);
    }

}
