package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.UserProfileRes;
import fi.istrange.traveler.api.UserProfileUpdateReq;
import fi.istrange.traveler.api.UserRegistrationReq;
import fi.istrange.traveler.auth.AuthorizedUser;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by arsenii on 4/9/17.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/users", tags = "users")
public class UserResource {

    // TODO DAO will go here as parameter
    public UserResource() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String registerUser(UserRegistrationReq userRegistrationReq) {
        // TODO upon successful registration generate token for the user
        throw new NotImplementedException();
    }

    @GET
    @PermitAll
    @Path("/profile")
    public UserProfileRes getUserProfile(@ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser) {
        // TODO use user DAO to get profile details
        throw new NotImplementedException();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/profile")
    public UserProfileRes updateUserProfile(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser,
            UserProfileUpdateReq userProfileUpdateReq) {
        // TODO use user DAO to update user's profile
        throw new NotImplementedException();
    }

    @DELETE
    @PermitAll
    @Path("/profile")
    public UserProfileRes deactivateUser(@ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser) {
        // TODO use user DAO to deactivate user's profile
        throw new NotImplementedException();
    }
}
