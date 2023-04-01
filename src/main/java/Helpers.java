import java.util.UUID;

public class Helpers {
    public static String generateFakeEmailAddress() {
        String uuid = UUID.randomUUID().toString();
        return "fakeemail_" + uuid + "@example.com";
    }
}
