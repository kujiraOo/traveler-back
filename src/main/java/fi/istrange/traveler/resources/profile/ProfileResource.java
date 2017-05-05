package fi.istrange.traveler.resources.profile;

import fi.istrange.traveler.api.*;
import fi.istrange.traveler.auth.Authenticator;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.dao.*;
import fi.istrange.traveler.db.tables.daos.CardDao;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.Card;
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

import static fi.istrange.traveler.dao.CredentialDao.updatePassword;

/**
 * Created by arsenii on 4/13/17.
 */
@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/profile", tags = "profile")
@PermitAll
public class ProfileResource {
    private final Authenticator auth;
    private final fi.istrange.traveler.db.tables.daos.CardDao cardDao;
    private final TravelerUserDao userDAO;
    private final UserPhotoDao userPhotoDao;
    private final CardPhotoDao cardPhotoDao;
    private final GroupCardParticipantDao participantDao;
    private final CustomCardDao customPersonalCardDao;

    @Inject
    public ProfileResource(
            ApplicationBundle applicationBundle
    ) {
        auth = new Authenticator();
        cardDao = new CardDao(applicationBundle.getJooqBundle().getConfiguration());
        userDAO = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
        participantDao = new GroupCardParticipantDao();
        customPersonalCardDao = new CustomCardDao();
        userPhotoDao = new UserPhotoDao(applicationBundle.getJooqBundle().getConfiguration().connectionProvider());
        cardPhotoDao = new CardPhotoDao(applicationBundle.getJooqBundle().getConfiguration().connectionProvider());
    }

    @GET
    @ApiOperation("Get user profile information")
    public UserProfileRes getUserProfile(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @Context DSLContext database
    ) {
        return UserProfileRes.fromEntity(
                this.userDAO.fetchOneByUsername(principal.getName()),
                userPhotoDao.fetchPhotoOidByUsername(principal.getName(), database)
        );
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Update user profile information")
    public UserProfileRes updateUserProfile(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            UserProfileUpdateReq userProfileUpdateReq,
            @Context DSLContext database
    ) {
        this.userDAO.update(
                fromUpdateReq(
                        userProfileUpdateReq,
                        this.userDAO.fetchOneByUsername(principal.getName())
                )
        );

        return getUserProfile(principal, database);
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

        updatePassword(principal.getName(), hashedPassword, database);

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

        return this.customPersonalCardDao.fetchByUsername(CardType.PERSONAL, principal.getName(), includeArchived, offset, database)
                .stream()
                .map(p -> buildPersonalCardRes(user, p, database))
                .collect(Collectors.toList());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("personal-cards/{id}")
    @ApiOperation("Update a personal card")
    public PersonalCardRes updatePersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId,
            PersonalCardUpdateReq cardUpdateReq,
            @Context DSLContext database
    ) {
        this.cardDao.update(
                fromUpdateReq(
                        cardUpdateReq,
                        cardDao.fetchOneById(personalCardId)
                )
        );

        return buildPersonalCardRes(principal.getName(), personalCardId, database);
    }

    @DELETE
    @Path("personal-cards/{id}")
    @ApiOperation("Archive a personal card by id")
    public PersonalCardRes deactivatePersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId,
            @Context DSLContext database
    ) {
        Card card = cardDao.fetchOneById(personalCardId);

        card.setActive(false);
        cardDao.update(card);

        return buildPersonalCardRes(principal.getName(), card, database);
    }

    private PersonalCardRes buildPersonalCardRes(TravelerUser user, Card card, DSLContext db) {
        return PersonalCardRes.fromEntity(
                card,
                user,
                userPhotoDao.fetchPhotoOidByUsername(user.getUsername(), db),
                cardPhotoDao.fetchPhotoOidByCardId(card.getId(), db)
        );
    }

    private PersonalCardRes buildPersonalCardRes(String userName, Card card, DSLContext db) {
        return buildPersonalCardRes(userDAO.fetchOneByUsername(userName), card, db);
    }

    private PersonalCardRes buildPersonalCardRes(String userName, Long cardId, DSLContext db) {
        return buildPersonalCardRes(userName, cardDao.fetchOneById(cardId), db);
    }

    @POST
    @Path("card-photos/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation("Upload a photo for a card")
    public Response uploadPersonalCardPhoto(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long cardId,
            @FormDataParam("file") InputStream uploadedPhotoStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @Context DSLContext database
    ) throws IOException, SQLException {
        cardPhotoDao.addPhoto(cardId, uploadedPhotoStream);

        return Response.accepted().build();
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
        return customPersonalCardDao
                .fetchByUsername(CardType.GROUP, principal.getName(), includeArchived, offset, database)
                .stream()
                .map(p -> buildGroupCardRes(principal.getName(), p, database))
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
        cardDao.update(fromUpdateReq(groupCardUpdateReq, cardDao.fetchOneById(cardId)));
        participantDao.updateGroupCardParticipants(cardId, groupCardUpdateReq.getParticipants(), database);

        return buildGroupCardRes(principal.getName(), cardId, database);
    }

    @DELETE
    @Path("group-cards/{id}")
    @ApiOperation("Archive a group travel card by id")
    public GroupCardRes deactivateGroupCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long cardId,
            @Context DSLContext database
    ) {
        Card card = cardDao.fetchOneById(cardId);

        card.setActive(false);
        cardDao.update(card);

        return buildGroupCardRes(principal.getName(), card, database);
    }

    @POST
    @Path("/photos")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation("Upload user photo")
    public UserProfileRes uploadPhoto(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @FormDataParam("file") InputStream uploadedPhotoStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @Context DSLContext database
    ) throws IOException, SQLException {

        userPhotoDao.addPhoto(principal.getName(), uploadedPhotoStream);
        return getUserProfile(principal, database);
    }


    private static TravelerUser fromUpdateReq (UserProfileUpdateReq req, TravelerUser user) {
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEmail(req.getEmail());
        user.setBirth(req.getBirthday());
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
        user.setCity(req.getCity());
        user.setCountry(req.getCountry());
        user.setGender(req.getGender());

        return user;
    }

    private GroupCardRes buildGroupCardRes(String userName, Long cardId, DSLContext db) {
        return buildGroupCardRes(userName, cardDao.fetchOneById(cardId), db);
    }

    private GroupCardRes buildGroupCardRes(String userName, Card card, DSLContext db) {
        return GroupCardRes.fromEntity(
                card,
                participantDao.getGroupCardParticipants(card.getId(), db, userDAO),
                userDAO.fetchOneByUsername(userName),
                userPhotoDao.fetchPhotoOidByUsername(userName, db),
                cardPhotoDao.fetchPhotoOidByCardId(card.getId(), db)
        );
    }

    private static Card fromUpdateReq(CardUpdateReq req, Card card) {
        card.setStartTime(req.getStartTime());
        card.setEndTime(req.getEndTime());
        card.setTitle(req.getTitle());
        card.setDescription(req.getDescription());
        card.setLon(req.getLon());
        card.setLat(req.getLat());

        return card;
    }
}
