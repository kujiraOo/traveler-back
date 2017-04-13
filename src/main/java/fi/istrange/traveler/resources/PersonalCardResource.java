package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.CardCreationReq;
import fi.istrange.traveler.api.CardRes;
import fi.istrange.traveler.api.CardUpdateReq;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.tables.daos.PersonalCardDao;
import fi.istrange.traveler.db.tables.pojos.PersonalCard;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.*;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by arsenii on 4/7/17.
 */
@Path("/personal-cards")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/personal-cards", tags = "personal cards")
@PermitAll
public class PersonalCardResource {
    PersonalCardDao cardDAO;

    public PersonalCardResource(
            ApplicationBundle applicationBundle
    ) {
        this.cardDAO = new PersonalCardDao(applicationBundle.getJooqBundle().getConfiguration());
    }

    @GET
    @ApiOperation(value = "Produces list of personal travel cards aggregated by radius")
    public List<CardRes> getPersonalCards(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @NotNull @QueryParam("lat") double lat,
            @NotNull @QueryParam("lng") double lng
    ) {
        // TODO get cards in radius of N kilometers for specified lon and lat

        return this.cardDAO.fetchByUsernameFk(principal.getName())
                .stream().map(CardRes::fromEntity)
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create new personal card")
    public CardRes createPersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            CardCreationReq personalCardCreationReq
    ) {
        this.cardDAO.insert(fromCreateReq(personalCardCreationReq, principal.getName()));

        return CardRes.fromEntity(this.cardDAO.fetchOneById(personalCardCreationReq.getId().intValue()));
    }

    @GET
    @Path("/{id}")
    @ApiOperation("Get a personal card by id")
    public Optional<CardRes> getPersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId
    ) {
        return this.cardDAO.fetchByUsernameFk(principal.getName())
                .stream()
                .filter(p -> p.getId() == personalCardId)
                .map(CardRes::fromEntity)
                .findFirst();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @ApiOperation("Update a personal card")
    public CardRes updatePersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId,
            CardUpdateReq cardUpdateReq
    ) {
        this.cardDAO.update(
                fromUpdateReq(
                        cardUpdateReq,
                        cardDAO.fetchOneById((int)personalCardId)
                )
        );

        return CardRes.fromEntity(this.cardDAO.fetchOneById((int) personalCardId));
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation("Delete personal card")
    public CardRes deactivatePersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId
    ) {
        CardRes res = CardRes.fromEntity(cardDAO.fetchOneById((int)personalCardId));
        this.cardDAO.deleteById((int) personalCardId);

        return res;
    }

    private static PersonalCard fromCreateReq(CardCreationReq req, String username) {
        return new PersonalCard(
                req.getId().intValue(),
                req.getStartTime(),
                req.getEndTime(),
                req.getLon(),
                req.getLat(),
                username
        );
    }

    private static PersonalCard fromUpdateReq(CardUpdateReq req, PersonalCard card) {
        card.setStartTime(req.getStartTime());
        card.setEndTime(req.getEndTime());
        card.setLon(req.getLon());
        card.setLat(req.getLat());

       return card;
    }
}
