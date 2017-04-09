package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.PersonalCardCreationReq;
import fi.istrange.traveler.api.PersonalCardRes;
import fi.istrange.traveler.api.PersonalCardUpdateReq;
import fi.istrange.traveler.auth.AuthorizedUser;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by arsenii on 4/7/17.
 */
@Path("/personal-cards")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/personal-cards", tags = "personal cards")
@PermitAll
public class PersonalCardResource {

    // TODO DAO will go here as parameter
    public PersonalCardResource() {
    }

    @GET
    @ApiOperation(value = "Produces list of personal travel cards", authorizations = @Authorization(
            value = "auth_scheme", scopes = @AuthorizationScope(
            scope = "user", description = "Write access to user data")))
    public List<PersonalCardRes> getPersonalCards(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser) {
        // TODO access DAO here and get list of personal cards
        throw new NotImplementedException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public PersonalCardRes createPersonalCard(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser,
            PersonalCardCreationReq personalCardCreationReq) {
        // TODO put new card in the db
        throw new NotImplementedException();
    }

    @GET
    @Path("/{id}")
    public PersonalCardRes getPersonalCard(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser,
            @PathParam("id") long personalCardId) {
        // TODO get card from the db
        throw new NotImplementedException();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public PersonalCardRes updatePersonalCard(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser,
            @PathParam("id") long personalCardId,
            PersonalCardUpdateReq personalCardUpdateReq) {
        // TODO update card in the db
        throw new NotImplementedException();
    }

    @DELETE
    @Path("/{id}")
    public PersonalCardRes deactivatePersonalCard(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser,
            @PathParam("id") long personalCardId) {
        // TODO deactivate card record
        throw new NotImplementedException();
    }
}
