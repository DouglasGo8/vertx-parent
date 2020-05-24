package br.com.cvc.hotel.broker.api;

import br.com.cvc.hotel.broker.domain.hotel.HotelInfoReq;
import br.com.cvc.hotel.broker.domain.vo.TravelInfoResp;
import br.com.cvc.hotel.broker.domain.vo.TravelOfHotelOps;
import io.vertx.axle.core.eventbus.EventBus;
import io.vertx.axle.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author dbatista
 */
@Path("/travel")
public class TravelResource {

    @Inject
    EventBus eventBus;
    @ConfigProperty(name = "vertxQ")
    private String vertxQ;

    @GET
    @Path("/sayHello")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<JsonObject> sayHi() {
        return CompletableFuture.supplyAsync(() -> new JsonObject().put("message", "Hi CVC"));
    }

    @POST
    @Path("/in")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<TravelInfoResp> travelReservation(@Valid HotelInfoReq hotelInfoReq) {
        return this.eventBus.<JsonObject>request(this.vertxQ, hotelInfoReq)
                .thenApply(Message::body)
                .thenApply(json -> json.mapTo(TravelInfoResp.class));
    }

}