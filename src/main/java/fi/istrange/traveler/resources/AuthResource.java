package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.UserCredentialsView;
import fi.istrange.traveler.auth.Authenticator;
import fi.istrange.traveler.bundle.ApplicationBundle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookiePrincipal;
import org.jooq.DSLContext;

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
    private Authenticator auth;

    public AuthResource() {
        auth = new Authenticator();
    }

    @POST
    @ApiOperation("Log in")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public DefaultJwtCookiePrincipal login(
            @Context ContainerRequestContext requestContext,
            @NotNull UserCredentialsView userCredentialsView,
            @Context DSLContext database
    ) {
        auth.authenticate(userCredentialsView.getName(), userCredentialsView.getPassword(), database);

        DefaultJwtCookiePrincipal principal = new DefaultJwtCookiePrincipal(userCredentialsView.getName());
        principal.addInContext(requestContext);
        return principal;
    }

    @DELETE
    @ApiOperation("Log out")
    public void logout(@Context ContainerRequestContext requestContext){
        JwtCookiePrincipal.removeFromContext(requestContext);
    }
}
