package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.PersonalCardRes;
import fi.istrange.traveler.api.GroupCardRes;
import fi.istrange.traveler.api.UserProfileRes;
import fi.istrange.traveler.api.UserProfileUpdateReq;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.tables.daos.GroupCardDao;
import fi.istrange.traveler.db.tables.daos.PersonalCardDao;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.*;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by arsenii on 4/13/17.
 */
@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/profile", tags = "profile")
@PermitAll
public class ProfileResource {

    private final TravelerUserDao userDAO;
    private final PersonalCardDao personalCardDao;
    private final GroupCardDao groupCardDao;

    @Inject
    public ProfileResource(
            ApplicationBundle applicationBundle
    ) {
        userDAO = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
        personalCardDao = new PersonalCardDao(applicationBundle.getJooqBundle().getConfiguration());
        groupCardDao = new GroupCardDao(applicationBundle.getJooqBundle().getConfiguration());
    }

    @GET
    @ApiOperation("Get user profile information")
    public UserProfileRes getUserProfile(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal
    ) {
        return UserProfileRes.fromEntity(this.userDAO.fetchOneByUsername(principal.getName()));
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update user profile information")
    public UserProfileRes updateUserProfile(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            UserProfileUpdateReq userProfileUpdateReq
    ) {
        this.userDAO.update(
                fromUpdateReq(
                        userProfileUpdateReq,
                        this.userDAO.fetchOneByUsername(principal.getName())
                )
        );

        return getUserProfile(principal);
    }

    @DELETE
    @ApiOperation("Deactivate user")
    public UserProfileRes deactivateUser(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal
    ) {
        // TODO use user DAO to deactivate user's profile
        throw new NotImplementedException();
    }

    @GET
    @Path("/personal-cards")
    @ApiOperation(value = "Produces list of personal travel cards created by user")
    public List<PersonalCardRes> getPersonalCards(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal
    ) {
        TravelerUser user = userDAO.fetchOneByUsername(principal.getName());

        return this.personalCardDao.fetchByUsernameFk(principal.getName())
                .stream().map(p -> PersonalCardRes.fromEntity(p, user))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/group-cards")
    @ApiOperation(value = "Produces list of group travel cards created by user")
    public List<GroupCardRes> getGroupCards(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal
    ) {
        // TODO access DAO here and get list of group cards
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
}
