package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.UserRegistrationReq;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;
import fi.istrange.traveler.db.Tables;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jooq.DSLContext;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @Inject
    public UserResource(
            ApplicationBundle applicationBundle
    ) {
        userDAO = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create new user profile")
    public Response registerUser(
            UserRegistrationReq newUser,
            @Context DSLContext database
    ) {
        userDAO.insert(fromRegisterReq(newUser));
        database.insertInto(
                Tables.USER_CREDENTIALS,
                Tables.USER_CREDENTIALS.USERNAME,
                Tables.USER_CREDENTIALS.PASSWORD,
                Tables.USER_CREDENTIALS.ACTIVE
                )
                .values(newUser.getUsername(), newUser.getPassword(), true)
                .execute();
        
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
                req.getCountry(),
                req.getPhoto(),
                req.getFirstName(),
                req.getLastName()
        );
    }

}
