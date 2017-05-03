package fi.istrange.traveler.resources.profile;

import fi.istrange.traveler.api.*;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.dao.CardPhotoDao;
import fi.istrange.traveler.dao.CustomCardDao;
import fi.istrange.traveler.dao.MatchCustomDao;
import fi.istrange.traveler.dao.UserPhotoDao;
import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by rohan on 4/22/17.
 */
@Path("/profile/cards")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/profile/cards/", tags = "match traveller cards")
@PermitAll
public class MatchResource {
    private final TravelerUserDao userDAO;
    private final UserPhotoDao userPhotoDao;
    private final CardPhotoDao cardPhotoDao;

    public MatchResource(
            ApplicationBundle applicationBundle
    ) {
        this.userDAO = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
        this.userPhotoDao = new UserPhotoDao(applicationBundle.getJooqBundle().getConfiguration().connectionProvider());
        this.cardPhotoDao = new CardPhotoDao(applicationBundle.getJooqBundle().getConfiguration().connectionProvider());
    }

    /**
     * Record a like.
     * A valid request must provide:
     * <pre>
     *     + valid card ids referring to valid active cards
     *     + two cards must have a shared active period !!
     *     + two cards must have a certain closeness in location? !!
     *     + liker card id must be associated with the Principal making the request
     *     + liked card id must not be associated with the Principal making the request
     *     A principal is said to be associated with a card when either he is the owner
     *     of the card (case personal travel card and group travel card), or he is
     *     the participant of the card (case group travel card).
     * </pre>
     *
     * @return
     * @throws BadRequestException if given invalid request
     */
    @ApiOperation(value = "Record that one card like other card")
    @PUT
    @Path("/{myCardId}/like/{cardId}")
    public MatchResultRes like(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("myCardId") @NotNull Long myCardId,
            @PathParam("cardId") @NotNull Long likedCardId,
            @Context DSLContext db
    ) {
        if (!validate(principal.getName(), myCardId, likedCardId, db)) {
            throw new BadRequestException("Dump request");
        }

        MatchCustomDao.createOrUpdateLike(myCardId, likedCardId, true, db);

        return new MatchResultRes(MatchCustomDao.isMatch(myCardId, likedCardId, db));
    }

    /**
     * Record a dislike
     *
     * @see MatchResource#
     */
    @ApiOperation(value = "Record that one card like other card")
    @PUT // side-effects of N > 0 identical requests is the same as for a single request
    @Path("/{myCardId}/dislike/{cardId}")
    public MatchResultRes dislike(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("myCardId") @NotNull Long myCardId,
            @PathParam("cardId") @NotNull Long likedCardId,
            @Context DSLContext db
    ) {
        if (!validate(principal.getName(), myCardId, likedCardId, db)) {
            throw new BadRequestException("Dump request");
        }

        MatchCustomDao.createOrUpdateLike(myCardId, likedCardId, false, db);

        return new MatchResultRes(MatchCustomDao.isMatch(myCardId, likedCardId, db));
    }


    /**
     * Get matching card for the card identified by the given id
     * A valid request must provide:
     * <pre>
     *     + valid card id referring to active card
     *     + card id is associated with the Principal
     *     making the request
     * </pre>
     * A card is said to be matched with given card iff two cards
     * liked each others
     *
     * @return
     * @throws BadRequestException if given invalid request
     */
    @ApiOperation(value = "Get the matched cards for given card")
    @GET
    @Path("/matches/{myCardId}")
    public MatchForCardRes getMatching(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("myCardId") @NotNull Long myCardId,
            @Context final DSLContext db
    ) {
        if (!CustomCardDao.isUserAssociatedWithCard(principal.getName(), myCardId, db)) {
            throw new BadRequestException();
        }
        Map<Boolean, List<Long>> matches = MatchCustomDao.getMatchingFor(myCardId, db)
                .stream()
                .collect(Collectors.partitioningBy(p -> CustomCardDao.isPersonalTravelCard(p, db)));

        List<PersonalCardRes> personalCardRess = createPersonalCardRes(matches.get(true), db);

        List<GroupCardRes> groupCardRess = createGroupCardRes(matches.get(false), db);

        return new MatchForCardRes(personalCardRess, groupCardRess);
    }

    /**
     * Check if two cards matched
     * Valid request must have the Principal associated with myCardId
     */
    @ApiOperation(value = "Check if there is a match between two cards")
    @GET
    @Path("/{myCarId}/{cardId}")
    public MatchResultRes isAMatch(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("myCarId") @NotNull Long myCardId,
            @PathParam("cardId") @NotNull Long likedCardId,
            @Context DSLContext db
    ) {
        if (!CustomCardDao.isUserAssociatedWithCard(principal.getName(), myCardId, db)) {
            throw new BadRequestException();
        }
        return new MatchResultRes(MatchCustomDao.isMatch(myCardId, likedCardId, db));
    }

    private List<PersonalCardRes> createPersonalCardRes(List<Long> personalCardIds, DSLContext db) {
        return
                db.selectFrom(Tables.CARD)
                        .where(Tables.CARD.ID.in(personalCardIds))
                        .fetch()
                        .stream()
                        .map(pcRecord ->
                                new PersonalCardRes(
                                        pcRecord.get(Tables.CARD.ID),
                                        pcRecord.get(Tables.CARD.TITLE),
                                        pcRecord.get(Tables.CARD.DESCRIPTION),
                                        pcRecord.get(Tables.CARD.START_TIME),
                                        pcRecord.get(Tables.CARD.END_TIME),
                                        pcRecord.get(Tables.CARD.LON),
                                        pcRecord.get(Tables.CARD.LAT),
                                        UserProfileRes.fromEntity(
                                                userDAO.fetchOneByUsername(
                                                        pcRecord.get(Tables.CARD.OWNER_FK)
                                                ),
                                                userPhotoDao.fetchPhotoOidByUsername(pcRecord.get(Tables.CARD.OWNER_FK), db)
                                        ),
                                        cardPhotoDao.fetchPhotoOidByCardId(pcRecord.get(Tables.CARD.ID), db)
                                )
                        )
                        .collect(Collectors.toList());
    }

    private List<GroupCardRes> createGroupCardRes(List<Long> groupCardIds, DSLContext db) {
        return db.selectFrom(Tables.CARD)
                .where(Tables.CARD.ID.in(groupCardIds))
                .fetch()
                .stream()
                .map(record ->
                        new GroupCardRes(
                                record.get(Tables.CARD.ID),
                                record.get(Tables.CARD.TITLE),
                                record.get(Tables.CARD.DESCRIPTION),
                                record.get(Tables.CARD.START_TIME),
                                record.get(Tables.CARD.END_TIME),
                                record.get(Tables.CARD.LON),
                                record.get(Tables.CARD.LAT),
                                UserProfileRes.fromEntity(
                                        userDAO.fetchOneByUsername(
                                                record.get(Tables.CARD.OWNER_FK)
                                        ),
                                        userPhotoDao.fetchPhotoOidByUsername(record.get(Tables.CARD.OWNER_FK), db)
                                ),
                                db.select().
                                        from(Tables.CARD_USER)
                                        .where(Tables.CARD_USER.CARD_ID.equal(
                                                record.get(Tables.GROUP_CARD.ID)
                                        )).fetch(Tables.CARD_USER.USERNAME),
                                cardPhotoDao.fetchPhotoOidByCardId(record.get(Tables.CARD.ID), db)
                        )
                ).collect(Collectors.toList());
    }


    private static boolean validate(String userName, Long likerCardId, Long likedCardId, DSLContext db) {
        if (!CustomCardDao.isActiveTravelCard(likerCardId, db)) return false;
        if (!CustomCardDao.isActiveTravelCard(likedCardId, db)) return false;
        if (!CustomCardDao.isUserAssociatedWithCard(userName, likerCardId, db)) return false;
        if (CustomCardDao.isUserAssociatedWithCard(userName, likedCardId, db)) return false;
        return true;
    }
}
