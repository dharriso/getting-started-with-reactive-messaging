package io.smallrye;

import com.optiva.quarkus.nats.js.NatsJetStreamProducerService;
import com.optiva.quarkus.nats.message.NatsMessageBuilder;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.jboss.logging.Logger;
import Response.ChargingResponse;
import Response.NatSubjectLine;
import EventMediator.EventMediator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Path("/eventmediator")
public class GreetingResource {
    private static final Logger LOG = Logger.getLogger(GreetingResource.class);

    @Inject
    NatsJetStreamProducerService jsService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_JSON, "application/problem+json"})
    public Uni<Response> mediateEvent(@Context UriInfo uriInfo,
                                          @Context HttpHeaders headers, @NotNull String chargingDataRequest) {
        NatsMessageBuilder msg = NatsMessageBuilder.create().subject(NatSubjectLine.getSubjectLine("rootCustomerId"))
                .data(chargingDataRequest.getBytes(StandardCharsets.UTF_8));
        try {

            return  Uni.createFrom()
                   .future(jsService.getNatsConnection().jetStream().publishAsync(msg.build()))
                   .onItem().transform(resp -> ChargingResponse.getChargingDataResponse("{}", uriInfo, "uuid", ChargingResponse.Service.CREATE))
                   .onFailure().invoke(t -> LOG.error("Event mediator Release request failed", t))
                   .onFailure().recoverWithItem(ChargingResponse.getProblemDetailsResponse(uriInfo));

        } catch (IOException e) {
            return Uni.createFrom().nullItem();
        }
    }

}
