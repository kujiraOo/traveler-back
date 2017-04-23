package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.*;
import fi.istrange.traveler.auth.Authenticator;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.dao.*;
import fi.istrange.traveler.db.tables.daos.GroupCardDao;
import fi.istrange.traveler.db.tables.daos.PersonalCardDao;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.GroupCard;
import fi.istrange.traveler.db.tables.pojos.PersonalCard;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jooq.DSLContext;
import org.mindrot.jbcrypt.BCrypt;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
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
    private final Authenticator auth;
    private final TravelerUserDao userDAO;
    private final UserPhotoDao userPhotoDao;
    private final GroupCardParticipantDao participantDao;
    private final GroupCardCustomDao customGroupCardDao;
    private final PersonalCardCustomDao customPersonalCardDao;
    private final CredentialDao credentialDao;
    private final PersonalCardDao personalCardDao;
    private final GroupCardDao groupCardDao;

    @Inject
    public ProfileResource(
            ApplicationBundle applicationBundle
    ) {
        auth = new Authenticator();
        credentialDao = new CredentialDao();
        userDAO = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
        participantDao = new GroupCardParticipantDao();
        groupCardDao = new GroupCardDao(applicationBundle.getJooqBundle().getConfiguration());
        customGroupCardDao = new GroupCardCustomDao();
        personalCardDao = new PersonalCardDao(applicationBundle.getJooqBundle().getConfiguration());
        customPersonalCardDao = new PersonalCardCustomDao();
        userPhotoDao = new UserPhotoDao(applicationBundle.getJooqBundle().getConfiguration());
    }

    @GET
    @ApiOperation("Get user profile information")
    public UserProfileRes getUserProfile(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal
    ) {
        return UserProfileRes.fromEntity(
                this.userDAO.fetchOneByUsername(principal.getName())
        );
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

    @PUT
    @Path("/password")
    @ApiOperation("Change user's password")
    public Response changePassword(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            PasswordChangeReq request,
            @Context DSLContext database
    ) {
        auth.authenticate(principal.getName(), request.getOldPassword(), database);

        String hashedPassword = BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt());
;
        credentialDao.updatePassword(principal.getName(), hashedPassword, database);

        return Response.accepted().build();
    }

    @GET
    @Path("/personal-cards")
    @ApiOperation(value = "Produces list of personal travel cards created by user")
    public List<PersonalCardRes> getPersonalCards(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @QueryParam("includeArchived") @DefaultValue("false") boolean includeArchived,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @Context DSLContext database
    ) {
        TravelerUser user = userDAO.fetchOneByUsername(principal.getName());

        return this.customPersonalCardDao.fetchByUsername(principal.getName(), includeArchived, offset, database)
                .stream()
                .map(p -> PersonalCardRes.fromEntity(p, user))
                .collect(Collectors.toList());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("personal-cards/{id}")
    @ApiOperation("Update a personal card")
    public PersonalCardRes updatePersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId,
            PersonalCardUpdateReq cardUpdateReq
    ) {
        this.personalCardDao.update(
                fromUpdateReq(
                        cardUpdateReq,
                        personalCardDao.fetchOneById(personalCardId)
                )
        );

        return PersonalCardRes.fromEntity(
                this.personalCardDao.fetchOneById(personalCardId),
                userDAO.fetchOneByUsername(principal.getName())
        );
    }

    @DELETE
    @Path("personal-cards/{id}")
    @ApiOperation("Archive a personal card by id")
    public PersonalCardRes deactivatePersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId
    ) {
        PersonalCard card = personalCardDao.fetchOneById(personalCardId);

        card.setActive(false);
        personalCardDao.update(card);

        return PersonalCardRes.fromEntity(card, userDAO.fetchOneByUsername(principal.getName()));
    }

    @GET
    @Path("/group-cards")
    @ApiOperation(value = "Produces list of group travel cards created by user")
    public List<GroupCardRes> getGroupCards(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @QueryParam("includeArchived") @DefaultValue("false") boolean includeArchived,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @Context DSLContext database
            ) {
        return customGroupCardDao.fetchByUsername(principal.getName(), includeArchived, offset, database)
                .stream()
                .map(p -> GroupCardRes.fromEntity(
                        p,
                        participantDao.getGroupCardParticipants(p.getId(), database, userDAO),
                        principal.getName())
                )
                .collect(Collectors.toList());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("group-cards/{id}")
    @ApiOperation("Update a group travel card by id")
    public GroupCardRes updateGroupCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long cardId,
            GroupCardUpdateReq groupCardUpdateReq,
            @Context DSLContext database
    ) {
        groupCardDao.update(fromUpdateReq(groupCardUpdateReq, groupCardDao.fetchOneById(cardId)));
        participantDao.updateGroupCardParticipants(cardId, groupCardUpdateReq.getParticipants(), database);

        return GroupCardRes.fromEntity(
                groupCardDao.fetchOneById(cardId),
                participantDao.getGroupCardParticipants(cardId, database, userDAO),
                principal.getName()
        );
    }

    @DELETE
    @Path("group-cards/{id}")
    @ApiOperation("Archive a group travel card by id")
    public GroupCardRes deactivateGroupCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long cardId,
            @Context DSLContext database
    ) {
        GroupCard card = groupCardDao.fetchOneById(cardId);

        card.setActive(false);
        groupCardDao.update(card);

        return GroupCardRes.fromEntity(
                card,
                participantDao.getGroupCardParticipants(cardId, database, userDAO),
                principal.getName()
        );
    }


    @POST
    @Path("/photos")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation("Upload user photo")
    public UserProfileRes uploadPhoto(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @FormDataParam("file") InputStream uploadedPhotoStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail
    ) throws IOException, SQLException {

        userPhotoDao.addPhoto(principal.getName(), uploadedPhotoStream);
        return getUserProfile(principal);
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

    private static PersonalCard fromUpdateReq(PersonalCardUpdateReq req, PersonalCard card) {
        card.setStartTime(req.getStartTime());
        card.setEndTime(req.getEndTime());
        card.setLon(req.getLon());
        card.setLat(req.getLat());

        return card;
    }

    private static GroupCard fromUpdateReq(GroupCardUpdateReq req, GroupCard card) {
        card.setStartTime(req.getStartTime());
        card.setEndTime(req.getEndTime());
        card.setLon(req.getLon());
        card.setLat(req.getLat());

        return card;
    }
}
