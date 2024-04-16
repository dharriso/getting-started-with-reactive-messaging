package quickstart;

import com.optiva.quarkus.nats.message.NatsMessageBuilder;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Channel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import com.optiva.quarkus.nats.js.NatsJetStreamProducerService;
import com.optiva.quarkus.nats.message.NatsMessageBuilder;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class ReactiveMessagingExample {

    //@Outgoing("source")
    //public Multi<String> source() {
    //    return Multi.createFrom().items("hello", "from", "SmallRye", "reactive", "messaging");
   // }

    @Inject
    NatsJetStreamProducerService jsService;

    @Outgoing("source")
    public Multi<Message> newSource() {
        return Multi.createFrom().items(Message.of("hello"), Message.of("from"),
                Message.of("SmallRye"), Message.of("reactive"), Message.of("messaging"));
    }
    public Uni<Void> sendMessage(String payload) throws IOException {
        NatsMessageBuilder msg = NatsMessageBuilder.create().subject("test it")
                .data(payload.getBytes(StandardCharsets.UTF_8));
        // Uni.createFrom().future(js.publishAsync(nmb.build())).replaceWithVoid();
        return  Uni.createFrom().future(jsService.getNatsConnection().jetStream()
                .publishAsync(msg.build())).replaceWithVoid();
    }
    //@Incoming("source")
    //@Outgoing("processed-a")
    //public String toUpperCase(String payload) {
    //    return payload.toUpperCase();
   /// }

    //@Incoming("processed-a")
    //@Outgoing("processed-b")
    //public Multi<String> filter(Multi<String> input) {
      //  return input.select().where(item -> item.length() > 4);
    //}

    //@Incoming("processed-b")
    //public void sink(String word) {
      //  System.out.println(">> " + word);
    // }

}
