package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.UserAuthReq;
import io.swagger.annotations.Api;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by arsenii on 4/9/17.
 */
@Path("/tokens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/tokens", tags = "tokens")
public class TokenResource {

    // TODO token DAO goes here as parameter
    public TokenResource() {}

    @POST
    public DefaultJwtCookiePrincipal authorizeUser(@Context ContainerRequestContext requestContext, String name) {
        DefaultJwtCookiePrincipal principal = new DefaultJwtCookiePrincipal(name);
        principal.addInContext(requestContext);
        return principal;
    }
}
