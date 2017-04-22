package fi.istrange.traveler.auth;

import fi.istrange.traveler.dao.CredentialDao;
import fi.istrange.traveler.db.tables.pojos.UserCredentials;
import org.jooq.DSLContext;
import org.mindrot.jbcrypt.BCrypt;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;
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

        Optional<UserCredentials> credentials = credentialDao.fetchByUsername(username, database);

        if (!isCheckSuccessful(credentials, password)) {
            throw new NotAuthorizedException("Invalid credentials");
        }
    }

    private boolean isCheckSuccessful(
            Optional<UserCredentials> credentials,
            String password
    ) {
        return credentials.isPresent() &&
                BCrypt.checkpw(password, credentials.get().getPassword()) &&
                credentials.get().getActive();
    }
}
