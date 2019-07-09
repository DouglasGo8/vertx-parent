package io.vertx.pagseguro.api.rest.event;


import io.vertx.axle.core.eventbus.EventBus;
import io.vertx.axle.core.eventbus.Message;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

/**
 *
 */
@Path("/async")
public class GreetingProducerResource {

    @Inject
    private EventBus bus;


    /**
     *
     * @param name
     * @return
     */
    @GET
    @Path("{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> greeting(@PathParam("name") String name) {
        return bus.<String>send("greeting", name)
                .thenApply(Message::body);
    }
}
