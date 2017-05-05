package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.UserRegistrationReq;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.dao.CredentialDao;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.jooq.DSLContext;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by arsenii on 4/9/17.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/users", tags = "users")
public class UserResource {
    private final TravelerUserDao userDAO;
    private final CredentialDao credentialDao;

    @Inject
    public UserResource(
            ApplicationBundle applicationBundle
    ) {
        userDAO = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
        credentialDao = new CredentialDao();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create new user profile")
    public Response registerUser(
            UserRegistrationReq newUser,
            @Context DSLContext database
    ) {
        userDAO.insert(fromRegisterReq(newUser));

        String hashedPassword = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());

        credentialDao.addUser(newUser.getUsername(), hashedPassword, database);

        return Response.accepted().build();
    }

    @DELETE
    @ApiOperation("Deactivate user")
    public Response deactivateUser(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @Context DSLContext database
    ) {
        credentialDao.deactivateUser(principal.getName(), database);

        return Response.accepted().build();
    }

    private static TravelerUser fromRegisterReq (UserRegistrationReq req) {
        return new TravelerUser(
                req.getUsername(),
                req.getBirth(),
                req.getGender(),
                req.getEmail(),
                req.getPhone(),
                req.getAddress(),
                req.getCity(),
                req.getCountry(), //TODO: save photo somewhere else
                req.getFirstName(),
                req.getLastName()
        );
    }

}
