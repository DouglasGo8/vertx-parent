package io.vertx.pagseguro.quarkus.api.rest.streaming;

import io.vertx.axle.core.Vertx;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;


/**
 *
 */
@Path("/hello")
public class StreamingResource {

    /**
     *
     */
    @Inject
    private Vertx vertx;


    /**
     * @param name
     * @return
     */
    @GET
    @Path("{name}/streaming")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<String> greeting(@PathParam("name") String name) {
        return ReactiveStreams
                .fromPublisher(vertx.periodicStream(2000).toPublisher())
                .map(l -> String.format("Hello %s! (%s)%n", name, new Date()))
                .buildRs();
    }
}
