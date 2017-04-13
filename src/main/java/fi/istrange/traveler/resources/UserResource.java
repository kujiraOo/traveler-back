package fi.istrange.traveler.resources;

import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public Response registerUser(TravelerUser newUser) {
        userDAO.insert(newUser);

        return Response.accepted().build();
    }
/*
    private static TravelerUser fromRegisterReq (UserRegistrationReq req) {
        return new TravelerUser(
                req.getUsername(),
                req.getBirthday(),
                req.getGender(),
                req.email,
                req.phone,
                req.address,
                req.city,
                req.country,
                req.photo,
                req.firstName,
                req.lastName
        )
    }
   */
}
