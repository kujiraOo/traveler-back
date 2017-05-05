package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.GroupCardCreationReq;
import fi.istrange.traveler.api.GroupCardRes;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.dao.*;
import fi.istrange.traveler.db.tables.daos.CardDao;
import fi.istrange.traveler.db.tables.daos.GroupCardDao;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.Card;
import fi.istrange.traveler.db.tables.pojos.GroupCard;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.jooq.DSLContext;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by arsenii on 4/7/17.
 */
@Path("/group-cards")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/group-cards", tags = "group cards")
@PermitAll
public class GroupCardResource {
    private final CardDao cardDAO;
    private final GroupCardDao groupCardDAO;
    private final CustomCardDao customGroupCardDao;
    private final TravelerUserDao userDAO;
    private final GroupCardParticipantDao participantDAO;
    private final UserPhotoDao userPhotoDao;
    private final CardPhotoDao cardPhotoDao;

    public GroupCardResource(
            ApplicationBundle applicationBundle
    ) {
        this.cardDAO = new CardDao(applicationBundle.getJooqBundle().getConfiguration());
        this.groupCardDAO = new GroupCardDao(applicationBundle.getJooqBundle().getConfiguration());
        this.userDAO = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
        this.participantDAO = new GroupCardParticipantDao();
        this.customGroupCardDao = new CustomCardDao();
        this.userPhotoDao = new UserPhotoDao(applicationBundle.getJooqBundle().getConfiguration().connectionProvider());
        this.cardPhotoDao = new CardPhotoDao(applicationBundle.getJooqBundle().getConfiguration().connectionProvider());
    }

    @GET
    @ApiOperation(value = "Produces list of group travel cards aggregated by radius")
    public List<GroupCardRes> getGroupCards(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @NotNull @QueryParam("lat") BigDecimal lat,
            @NotNull @QueryParam("lng") BigDecimal lng,
            @QueryParam("includeArchived") @DefaultValue("false") boolean includeArchived,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @Context DSLContext database
    ) {
        return customGroupCardDao.fetchByPosition(CardType.GROUP, lat, lng, includeArchived, offset, database)
                .stream()
                .filter(p -> !p.getOwnerFk().equals(principal.getName()))
                .map(p -> GroupCardRes.fromEntity(
                        p,
                        participantDAO.getGroupCardParticipants(p.getId(), database, userDAO),
                        userDAO.fetchOneByUsername(principal.getName()),
                        userPhotoDao.fetchPhotoOidByUsername(principal.getName(), database),
                        cardPhotoDao.fetchPhotoOidByCardId(p.getId(), database)
                )).collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create a new group travel card")
    public GroupCardRes createGroupCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            GroupCardCreationReq groupCardCreationReq,
            @Context DSLContext database
    ) {
        Long cardId = cardDAO.count() + 1;
        cardDAO.insert(fromCreateReq(cardId, groupCardCreationReq, principal.getName()));
        groupCardDAO.insert(new GroupCard(cardId));
        groupCardCreationReq.getParticipants()
                .forEach(p -> participantDAO.addGroupCardParticipant(cardId, p, database));

        return getGroupCard(principal, cardId, database);
    }

    @GET
    @Path("/{id}")
    @ApiOperation("Get a specific group travel card by id")
    public GroupCardRes getGroupCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long cardId,
            @Context DSLContext database
    ) {
        return GroupCardRes.fromEntity(
                cardDAO.fetchOneById(cardId),
                participantDAO.getGroupCardParticipants(cardId, database, userDAO),
                userDAO.fetchOneByUsername(principal.getName()),
                userPhotoDao.fetchPhotoOidByUsername(principal.getName(), database),
                cardPhotoDao.fetchPhotoOidByCardId(cardId, database)
        );
    }

    private static Card fromCreateReq(Long cardId, GroupCardCreationReq req, String username) {
        return new Card(
                cardId,
                req.getStartTime(),
                req.getEndTime(),
                req.getLon(),
                req.getLat(),
                username,
                true,
                req.getTitle(),
                req.getDescription()
        );
    }
}
