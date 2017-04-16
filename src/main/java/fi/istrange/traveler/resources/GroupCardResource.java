package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.CardRes;
import fi.istrange.traveler.api.GroupCardCreationReq;
import fi.istrange.traveler.api.GroupCardRes;
import fi.istrange.traveler.api.GroupCardUpdateReq;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.dao.GroupCardParticipantDao;
import fi.istrange.traveler.db.tables.daos.GroupCardDao;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.GroupCard;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.jooq.DSLContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by arsenii on 4/7/17.
 */
@Path("/group-cards")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/group-cards", tags = "group cards")
@PermitAll
public class GroupCardResource {
    private final GroupCardDao cardDAO;
    private final TravelerUserDao userDAO;
    private final GroupCardParticipantDao participantDAO;

    public GroupCardResource(
            ApplicationBundle applicationBundle
    ) {
        this.cardDAO = new GroupCardDao(applicationBundle.getJooqBundle().getConfiguration());
        this.userDAO = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
        this.participantDAO = new GroupCardParticipantDao();
    }

    @GET
    @ApiOperation(value = "Produces list of group travel cards aggregated by radius")
    public List<CardRes> getGroupCards(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @NotNull @QueryParam("lat") double lat,
            @NotNull @QueryParam("lng") double lng
    ) {
        // TODO get cards in radius of N kilometers for specified lon and lat

        throw new NotImplementedException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public GroupCardRes createGroupCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            GroupCardCreationReq groupCardCreationReq,
            @Context DSLContext database
    ) {
        cardDAO.insert(fromCreateReq(groupCardCreationReq, principal.getName()));
        groupCardCreationReq.getParticipants()
                .forEach(p -> participantDAO.addGroupCardParticipant(groupCardCreationReq.getId(), p, database));

        return getGroupCard(principal, groupCardCreationReq.getId(), database);
    }

    @GET
    @Path("/{id}")
    public GroupCardRes getGroupCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long cardId,
            @Context DSLContext database
    ) {
        return GroupCardRes.fromEntity(
                cardDAO.fetchOneById(cardId),
                participantDAO.getGroupCardParticipants(cardId, database, userDAO),
                principal.getName()
        );
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public GroupCardRes updateGroupCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long cardId,
            GroupCardUpdateReq groupCardUpdateReq,
            @Context DSLContext database
    ) {
        cardDAO.update(fromUpdateReq(groupCardUpdateReq, cardDAO.fetchOneById(cardId)));
        participantDAO.updateGroupCardParticipants(cardId, groupCardUpdateReq.getParticipants(), database);

        return getGroupCard(principal, cardId, database);
    }

    @DELETE
    @Path("/{id}")
    public GroupCardRes deactivateGroupCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId
    ) {
        // TODO deactivate card record
        throw new NotImplementedException();
    }

    private static GroupCard fromCreateReq(GroupCardCreationReq req, String username) {
        return new GroupCard(
                req.getId(),
                req.getStartTime(),
                req.getEndTime(),
                req.getLon(),
                req.getLat(),
                username
        );
    }

    private static GroupCard fromUpdateReq(GroupCardUpdateReq req, GroupCard card) {
        card.setStartTime(req.getStartTime());
        card.setEndTime(req.getEndTime());
        card.setLon(req.getLon());
        card.setLat(req.getLat());

        return card;
    }
}
