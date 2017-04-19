package fi.istrange.traveler.auth;

import fi.istrange.traveler.dao.CredentialDao;
import fi.istrange.traveler.db.tables.pojos.UserCredentials;
import org.jooq.DSLContext;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Created by aleksandr on 18.4.2017.
 */
public class Authenticator {
    private CredentialDao credentialDao;

    public Authenticator() {
        credentialDao = new CredentialDao();
    }

    public void authenticate(
            String username,
            String password,
            DSLContext database
    ) throws WebApplicationException {
        if (username == null || password == null) {
            throw new WebApplicationException(422);
        }

        String hashedPassword = password;
        try {
            hashedPassword = PasswordHasher.hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Optional<UserCredentials> credentials = credentialDao.fetchByUsername(username, database);

        if (!isCheckSuccessful(credentials, hashedPassword)) {
            throw new NotAuthorizedException("Invalid credentials");
        }
    }

    private boolean isCheckSuccessful(
            Optional<UserCredentials> credentials,
            String hashedPassword
    ) {
        return credentials.isPresent() &&
                hashedPassword.equals(credentials.get().getPassword()) &&
                credentials.get().getActive();
    }
}
