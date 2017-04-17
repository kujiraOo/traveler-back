package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.PersonalCardCreationReq;
import fi.istrange.traveler.api.PersonalCardRes;
import fi.istrange.traveler.api.PersonalCardUpdateReq;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.dao.PersonalCardLocationDao;
import fi.istrange.traveler.db.tables.daos.PersonalCardDao;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.PersonalCard;
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
    private final PersonalCardDao cardDAO;
    private final TravelerUserDao userDAO;
    private final PersonalCardLocationDao locationDao;

    public PersonalCardResource(
            ApplicationBundle applicationBundle
    ) {
        this.cardDAO = new PersonalCardDao(applicationBundle.getJooqBundle().getConfiguration());
        this.userDAO = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
        this.locationDao = new PersonalCardLocationDao();
    }

    @GET
    @ApiOperation(value = "Produces list of personal travel cards aggregated by radius")
    public List<PersonalCardRes> getPersonalCards(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @NotNull @QueryParam("lat") BigDecimal lat,
            @NotNull @QueryParam("lng") BigDecimal lng,
            @QueryParam("includeArchived") @DefaultValue("false") boolean includeArchived,
            @QueryParam("offset") @DefaultValue("0") long offset,
            @Context DSLContext database
            ) {
        return locationDao.getCardsByLocation(lat, lng, database)
                .stream()
                .filter(p -> p.getActive() || includeArchived)
                .skip(offset)
                .limit(20)
                .map(p -> PersonalCardRes.fromEntity(p, userDAO.fetchOneByUsername(principal.getName())))
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create new personal card")
    public PersonalCardRes createPersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            PersonalCardCreationReq personalCardCreationReq
    ) {
        this.cardDAO.insert(fromCreateReq(personalCardCreationReq, principal.getName()));

        return PersonalCardRes.fromEntity(
                this.cardDAO.fetchOneById(personalCardCreationReq.getId()),
                userDAO.fetchOneByUsername(principal.getName())
        );
    }

    @GET
    @Path("/{id}")
    @ApiOperation("Get a personal card by id")
    public Optional<PersonalCardRes> getPersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId
    ) {
        return this.cardDAO.fetchByUsernameFk(principal.getName())
                .stream()
                .filter(p -> p.getId() == personalCardId)
                .map(p -> PersonalCardRes.fromEntity(p, userDAO.fetchOneByUsername(principal.getName())))
                .findFirst();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @ApiOperation("Update a personal card")
    public PersonalCardRes updatePersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId,
            PersonalCardUpdateReq cardUpdateReq
    ) {
        this.cardDAO.update(
                fromUpdateReq(
                        cardUpdateReq,
                        cardDAO.fetchOneById(personalCardId)
                )
        );

        return PersonalCardRes.fromEntity(
                this.cardDAO.fetchOneById(personalCardId),
                userDAO.fetchOneByUsername(principal.getName())
        );
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation("Archive a personal card by id")
    public PersonalCardRes deactivatePersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId
    ) {
        PersonalCard card = cardDAO.fetchOneById(personalCardId);

        card.setActive(false);
        cardDAO.update(card);

        return PersonalCardRes.fromEntity(card, userDAO.fetchOneByUsername(principal.getName()));
    }

    private static PersonalCard fromCreateReq(PersonalCardCreationReq req, String username) {
        return new PersonalCard(
                req.getId(),
                req.getStartTime(),
                req.getEndTime(),
                req.getLon(),
                req.getLat(),
                username,
                true
        );
    }

    private static PersonalCard fromUpdateReq(PersonalCardUpdateReq req, PersonalCard card) {
        card.setStartTime(req.getStartTime());
        card.setEndTime(req.getEndTime());
        card.setLon(req.getLon());
        card.setLat(req.getLat());

       return card;
    }
}
