package fi.istrange.traveler.auth;

import java.security.Principal;

/**
 * Created by arsenii on 4/8/17.
 */

//TODO We can try to use domain class instead, but implementing Principal interface is dropwizard's requirement
public class AuthorizedUser implements Principal{

    @Override
    public String getName() {
        return null;
    }
}
