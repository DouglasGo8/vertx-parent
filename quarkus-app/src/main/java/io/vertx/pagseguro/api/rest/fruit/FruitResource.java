package io.vertx.pagseguro.api.rest.fruit;


import io.vertx.pagseguro.domain.fruit.Fruit;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 *
 */
@Path("/fruits")
@NoArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitResource {

    private String host;

    /**
     *
     */
    @Inject
    private Fruit fruit;

    // @Inject
    // @ConfigProperty(name="myapp.schema.create", defaultValue = "true")
    // boolean schemaCreate;

    /**
     * @throws UnknownHostException
     */
    @PostConstruct
    public void setUp() throws UnknownHostException {

        this.host = InetAddress.getLocalHost().getHostName();
        // if (schemaCreate) // postgres schema must be created

    }


    /**
     * @return
     */
    @GET
    @Path("/greet")
    public CompletionStage<Response> greetings() {
        return CompletableFuture.<String>supplyAsync(() -> String.format("Hello from %s\n", this.host))
                .thenApply(Response::ok)
                .thenApply(ResponseBuilder::build);
    }

    /**
     * @return
     */
    @GET
    @Path("/list")
    public CompletionStage<Response> findAll() {
        return this.fruit.findAll()
                .thenApply(Response::ok)
                .thenApply(ResponseBuilder::build);
    }

    /**
     *
     * @param id
     * @return
     */
    @GET
    @Path("/{id}")
    public CompletionStage<Response> findOne(@PathParam("id") Long id) {
        return this.fruit.findById(id)
                .thenApply(Response::ok)
                .thenApply(ResponseBuilder::build);
    }

    /**
     *
     * @param fruit
     * @return
     */
    @POST
    @Path("/save")
    public CompletionStage<Response> saveOne(Fruit fruit) {
        return this.fruit.save(fruit)
                // HATEOAS
                .thenApply(json -> URI.create("/fruits/" + json.getLong("id")))
                .thenApply(uri -> Response.created(uri).build());

    }

    /**
     *
     * @param fruit
     * @return
     */
    @PUT
    public CompletionStage<Response> update(Fruit fruit) {
        return this.fruit.update(fruit)
                .thenApply(json -> json.getBoolean("isRowsAffected") ? Status.OK : Status.NOT_FOUND)
                .thenApply(status -> Response.status(status).build());
    }

    /**
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("/{id}")
    public CompletionStage<Response> delete(@PathParam("id") Long id) {
        return this.fruit.delete(id)
                .thenApply(json -> json.getBoolean("isRowsAffected") ? Status.NO_CONTENT : Status.NOT_FOUND)
                .thenApply(status -> Response.status(status).build());
    }

}
