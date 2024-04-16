package Response;

public class JsonOCEResponse {
    private int httpStatusCode;
    private String invocationTimestamp;
    private int invocationSequenceNumber;

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(final int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getInvocationTimestamp() {
        return invocationTimestamp;
    }

    public void setInvocationTimestamp(final String invocationTimestamp) {
        this.invocationTimestamp = invocationTimestamp;
    }

    public int getInvocationSequenceNumber() {
        return invocationSequenceNumber;
    }

    public void setInvocationSequenceNumber(final int invocationSequenceNumber) {
        this.invocationSequenceNumber = invocationSequenceNumber;
    }
}
