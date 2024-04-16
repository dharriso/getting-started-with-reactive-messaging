package Response;

import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChargingResponse {

    private static final Logger logger = Logger.getLogger(ChargingResponse.class);

    public static final String INVOCATION_TIMESTAMP = "invocationTimestamp";
    public static final String INVOCATION_SEQUENCE_NUMBER = "invocationSequenceNumber";

    public enum Service {
        CREATE,
        UPDATE,
        RELEASE
    }
    private ChargingResponse() {}
//    public static Uni<Response> getChargingDataUniResponse(String jsonString, UriInfo uriInfo, String uuid, Service service) {
//        //jakarta.ws.rs.core.Response resp = getChargingDataResponse(jsonString, uriInfo, uuid, service);
//        return Uni.createFrom().item(getChargingDataResponse(jsonString, uriInfo, uuid, service));
//    }

    public static Response getChargingDataResponse(String jsonString, UriInfo uriInfo, String uuid, Service service) {
        logger.info("ChargingDataResponse: Response received for Msg-Id"+uuid);
        JsonObject jsonDeserialized = new JsonObject(jsonString);
//
//        int statusCode = jsonDeserialized.getInteger("httpStatusCode");
         var statusCode = 201;
        if (statusCode >= Response.Status.OK.getStatusCode() &&
                statusCode < Response.Status.MULTIPLE_CHOICES.getStatusCode()) {
            Response.Status statusCode1 =  getOkStatusCode(service);

            switch( statusCode1) {
                case CREATED -> {
                    return Response.status(statusCode1)
                        .entity(ChargingResponse.getChargingDataResponse(jsonDeserialized))
                        // create the header based on the incoming scheme, host, path and the generated uuid
                        .header("Location", uriInfo.getAbsolutePath().toString() + "/" + uuid)
                        .build();
                }
                case OK -> {
                    return Response.status(statusCode1)
                            .entity(ChargingResponse.getChargingDataResponse(jsonDeserialized))
                            .build();
                }
                default -> {
                    return Response.status(statusCode1).build();
                }
            }
        }
        return Response.status(statusCode)
                .entity(ChargingResponse.getProblemDetailsResponse(uriInfo))
                // create the header based on the incoming scheme, host, path and the generated uuid
                //.header("Location", uriInfo.getAbsolutePath().toString() + "/" + uuid)
                .build();
    }

    private static Response.Status getOkStatusCode(final Service service) {
        switch (service) {
            case CREATE:
                return Response.Status.CREATED;
            case RELEASE:
                return Response.Status.NO_CONTENT;
            case UPDATE:
            default:
                return Response.Status.OK;
        }
    }

    public static Response getChargingDataResponse(UriInfo uriInfo, String uuid) {
        return Response.status(Response.Status.CREATED)
                .entity(ChargingResponse.getChargingDataResponse())
                // create the header based on the incoming scheme, host, path and the generated uuid
                .header("Location", uriInfo.getAbsolutePath().toString() + "/" + uuid)
                .build();
    }
    public static Response getChargingDataReleaseResponse() {
        return Response.status(Response.Status.OK)
                .entity(ChargingResponse.getChargingDataResponse())
                // create the header based on the incoming scheme, host, path and the generated uuid
                .build();
    }
    public static Response getChargingDataUpdateResponse() {
        return Response.status(Response.Status.OK)
                .entity(ChargingResponse.getChargingDataResponse())
                // create the header based on the incoming scheme, host, path and the generated uuid
                .build();
    }
    public static String getChargingDataResponse() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String formattedDateTime = currentDateTime.format(formatter);
        return  new JsonObject()
                .put(INVOCATION_TIMESTAMP, formattedDateTime)
                .put(INVOCATION_SEQUENCE_NUMBER, 123456).encode();
    }

    public static String getChargingDataResponse(JsonObject jsonDeserialized) {

        return  new JsonObject()
                .put(INVOCATION_TIMESTAMP, jsonDeserialized.getString(INVOCATION_TIMESTAMP))
                .put(INVOCATION_SEQUENCE_NUMBER, jsonDeserialized.getInteger(INVOCATION_SEQUENCE_NUMBER)).encode();
    }
    public static Response getProblemDetailsResponse(UriInfo uriInfo) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ChargingResponse.getProblemDetails(uriInfo))
                // create the header based on the incoming scheme, host, path and the generated uuid
                //.header("Location", uriInfo.getAbsolutePath().toString()+"/some.uuid/")
                .build();
    }
    public static String getProblemDetails(UriInfo uriInfo) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String formattedDateTime = currentDateTime.format(formatter);
        return  new JsonObject()
                .put("type", "https://example.com/5g-problems/server-error")
                .put("title", "Internal Server Error")
                .put("status", Response.Status.INTERNAL_SERVER_ERROR)
                .put("detail", "There was an problem storing the request.")
                .put("instance", uriInfo.getPath())
                .put( "timestamp", formattedDateTime)
                .encode();
    }
}
