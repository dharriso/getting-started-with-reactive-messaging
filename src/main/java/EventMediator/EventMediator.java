package EventMediator;

import Response.NatSubjectLine;
import com.optiva.quarkus.nats.message.NatsMessageBuilder;
import io.smallrye.mutiny.Uni;

import java.nio.charset.StandardCharsets;

public class EventMediator {
    public static Uni<NatsMessageBuilder> buildNatsEvent(String jsonEvent) {
       return Uni.createFrom().item(NatsMessageBuilder.create().subject(NatSubjectLine.getSubjectLine("rootCustomerId"))
                .data(jsonEvent.getBytes(StandardCharsets.UTF_8)));
    }
}
