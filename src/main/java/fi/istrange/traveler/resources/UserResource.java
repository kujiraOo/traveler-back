package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.UserProfileRes;
import fi.istrange.traveler.api.UserProfileUpdateReq;
import fi.istrange.traveler.auth.AuthorizedUser;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
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

    @GET
    @PermitAll
    @Path("/profile")
    @ApiOperation("Get user profile information")
    public UserProfileRes getUserProfile(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser
    ) {
        return UserProfileRes.fromEntity(this.userDAO.fetchOneByUsername(authorizedUser.getName()));
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/profile")
    @ApiOperation("Update user profile information")
    public UserProfileRes updateUserProfile(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser,
            UserProfileUpdateReq userProfileUpdateReq) {
        this.userDAO.update(
                fromUpdateReq(
                        userProfileUpdateReq,
                        this.userDAO.fetchOneByUsername(authorizedUser.getName())
                )
        );

        return getUserProfile(authorizedUser);
    }

    @DELETE
    @PermitAll
    @Path("/profile")
    @ApiOperation("Deactivate user")
    public UserProfileRes deactivateUser(@ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser) {
        // TODO use user DAO to deactivate user's profile
        throw new NotImplementedException();
    }

    private static TravelerUser fromUpdateReq (UserProfileUpdateReq req, TravelerUser user) {
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setAddress(req.getAddress());
        user.setCity(req.getCity());
        user.setCountry(req.getCountry());
        user.setBirth(req.getBirthday());
        user.setGender(req.getGender());

        return user;
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
