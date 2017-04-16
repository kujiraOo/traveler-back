package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.UserCredentialsView;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.Tables;
import io.swagger.annotations.Api;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.dhatim.dropwizard.jwt.cookie.authentication.JwtCookiePrincipal;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

/**
 * Created by arsenii on 4/9/17.
 */

@Path("/auth")
@Api(value = "/auth", tags = "auth")
public class AuthResource {
    public AuthResource(ApplicationBundle applicationBundle) {
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public DefaultJwtCookiePrincipal login(
            @Context ContainerRequestContext requestContext,
            @NotNull UserCredentialsView userCredentialsView,
            @Context DSLContext database
    ) {
        if (userCredentialsView.getName() == null || userCredentialsView.getPassword() == null) {
            throw new WebApplicationException(422);
        }

        Result<Record> res = database.select()
                .from(Tables.USER_CREDENTIALS).fetch();
        System.out.println(res.size());
        for (Record record : res) {
            System.out.println(record);
        }

        Optional<Record> result = database.select()
                .from(Tables.USER_CREDENTIALS)
                .where(Tables.USER_CREDENTIALS.USERNAME.equal(userCredentialsView.getName()))
                .fetchOptional();

        if (
                !result.isPresent() ||
                !userCredentialsView.getPassword().equals(result.get().getValue(Tables.USER_CREDENTIALS.PASSWORD))
        ) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        DefaultJwtCookiePrincipal principal = new DefaultJwtCookiePrincipal(userCredentialsView.getName());
        principal.addInContext(requestContext);
        return principal;
    }

    @POST
    @Path("/logout")
    public void logout(@Context ContainerRequestContext requestContext){
        JwtCookiePrincipal.removeFromContext(requestContext);
    }
}
