package Response;

import java.util.Random;

public class NatSubjectLine {
    public static final int numberStreams = 1;
    public static final String STREAM_NAME_PREFIX = "Events";
    public static final String CLUSTER_NAME_PREFIX = "altdb01";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";


    public static String getSubjectLine(String subscriberIdentity) {
        var streamIndex  = subscriberIdentity.hashCode() % numberStreams;
        return String.format("%s%d.%s.%d", STREAM_NAME_PREFIX, streamIndex, CLUSTER_NAME_PREFIX,subscriberIdentity.hashCode());
    }

    private static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
