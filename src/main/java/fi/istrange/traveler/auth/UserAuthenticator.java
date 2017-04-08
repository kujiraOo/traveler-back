package fi.istrange.traveler.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import java.util.Optional;

/**
 * Created by arsenii on 4/8/17.
 */
public class UserAuthenticator implements Authenticator<String, AuthorizedUser> {

    @Override
    public Optional<AuthorizedUser> authenticate(String accessTokenId) throws AuthenticationException {

        //TODO Token DAO to validate the token and update token's last access time goes here, resolve user id and construct AuthorizedUser
        return Optional.of(new AuthorizedUser());
    }
}
