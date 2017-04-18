package fi.istrange.traveler.auth;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by aleksandr on 18.4.2017.
 */
public class PasswordHasher {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] passwordHash = digest
                .digest(
                        password
                                .getBytes(StandardCharsets.UTF_8)
                );
        String hashedPasswordString = DatatypeConverter.printHexBinary(
                passwordHash
        ).toLowerCase();

        return hashedPasswordString;
    }
}
