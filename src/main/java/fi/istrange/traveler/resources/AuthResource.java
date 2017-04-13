package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.UserCredentials;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import io.swagger.annotations.Api;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookiePrincipal;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by arsenii on 4/9/17.
 */

@Path("/auth")
@Api(value = "/auth", tags = "auth")
public class AuthResource {

    private final TravelerUserDao travelerUserDao;

    public AuthResource(ApplicationBundle applicationBundle) {
        travelerUserDao = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public DefaultJwtCookiePrincipal login(
            @Context ContainerRequestContext requestContext, @NotNull UserCredentials userCredentials) {

        if (userCredentials.getName() == null || userCredentials.getPassword() == null) {
            throw new WebApplicationException(422);
        }
        // TODO use DAO to get user's password by user's name
        String passwordFromDB = "secret";

        if (!userCredentials.getPassword().equals(passwordFromDB)) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        DefaultJwtCookiePrincipal principal = new DefaultJwtCookiePrincipal(userCredentials.getName());
        principal.addInContext(requestContext);
        return principal;
    }

    @POST
    @Path("/logout")
    public void logout(@Context ContainerRequestContext requestContext){
        JwtCookiePrincipal.removeFromContext(requestContext);
    }
}
