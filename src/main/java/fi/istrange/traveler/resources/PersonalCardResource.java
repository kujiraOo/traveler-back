package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.PersonalCardCreationReq;
import fi.istrange.traveler.api.PersonalCardRes;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.dao.CardPhotoDao;
import fi.istrange.traveler.dao.CardType;
import fi.istrange.traveler.dao.CustomCardDao;
import fi.istrange.traveler.dao.UserPhotoDao;
import fi.istrange.traveler.db.tables.daos.CardDao;
import fi.istrange.traveler.db.tables.daos.PersonalCardDao;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.Card;
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
    private final CardDao cardDAO;
    private final PersonalCardDao personalCardDao;
    private final TravelerUserDao userDAO;
    private final CustomCardDao customPersonalCardDao;
    private final UserPhotoDao userPhotoDao;
    private final CardPhotoDao cardPhotoDao;

    public PersonalCardResource(
            ApplicationBundle applicationBundle
    ) {
        this.cardDAO = new CardDao(applicationBundle.getJooqBundle().getConfiguration());
        this.personalCardDao = new PersonalCardDao(applicationBundle.getJooqBundle().getConfiguration());
        this.userDAO = new TravelerUserDao(applicationBundle.getJooqBundle().getConfiguration());
        this.customPersonalCardDao = new CustomCardDao();
        this.userPhotoDao = new UserPhotoDao(applicationBundle.getJooqBundle().getConfiguration());
        this.cardPhotoDao = new CardPhotoDao(applicationBundle.getJooqBundle().getConfiguration());
    }

    @GET
    @ApiOperation(value = "Produces list of personal travel cards aggregated by radius")
    public List<PersonalCardRes> getPersonalCards(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @NotNull @QueryParam("lat") BigDecimal lat,
            @NotNull @QueryParam("lng") BigDecimal lng,
            @QueryParam("includeArchived") @DefaultValue("false") boolean includeArchived,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @Context DSLContext database
            ) {
        return customPersonalCardDao.fetchByPosition(CardType.PERSONAL, lat, lng, includeArchived, offset, database)
                .stream()
                .map(p -> PersonalCardRes.fromEntity(
                        p,
                        userDAO.fetchOneByUsername(principal.getName()),
                        userPhotoDao.fetchByUsername(principal.getName(), database),
                        cardPhotoDao.fetchById(p.getId(), database)
                ))
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation("Create new personal card")
    public PersonalCardRes createPersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            PersonalCardCreationReq personalCardCreationReq,
            @Context DSLContext database
    ) {
        this.cardDAO.insert(fromCreateReq(personalCardCreationReq, principal.getName()));
        this.personalCardDao.insert(new PersonalCard(personalCardCreationReq.getId()));

        return PersonalCardRes.fromEntity(
                this.cardDAO.fetchOneById(personalCardCreationReq.getId()),
                userDAO.fetchOneByUsername(principal.getName()),
                userPhotoDao.fetchByUsername(principal.getName(), database),
                cardPhotoDao.fetchById(personalCardCreationReq.getId(), database)
        );
    }

    @GET
    @Path("/{id}")
    @ApiOperation("Get a personal card by id")
    public Optional<PersonalCardRes> getPersonalCard(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("id") long personalCardId,
            @Context DSLContext database
    ) {
        return this.cardDAO.fetchByOwnerFk(principal.getName())
                .stream()
                .filter(p -> p.getId() == personalCardId)
                .map(p -> PersonalCardRes.fromEntity(
                        p,
                        userDAO.fetchOneByUsername(principal.getName()),
                        userPhotoDao.fetchByUsername(principal.getName(), database),
                        cardPhotoDao.fetchById(personalCardId, database)
                ))
                .findFirst();
    }

    private static Card fromCreateReq(PersonalCardCreationReq req, String username) {
        return new Card(
                req.getId(),
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
