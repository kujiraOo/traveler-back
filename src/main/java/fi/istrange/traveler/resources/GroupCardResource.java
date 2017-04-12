package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.GroupCardCreationReq;
import fi.istrange.traveler.api.GroupCardRes;
import fi.istrange.traveler.api.GroupCardUpdateReq;
import fi.istrange.traveler.auth.AuthorizedUser;
import fi.istrange.traveler.bundle.ApplicationBundle;
import fi.istrange.traveler.db.tables.daos.GroupCardDao;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
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
    GroupCardDao cardDAO;

    public GroupCardResource(
            ApplicationBundle applicationBundle
    ) {
        this.cardDAO = new GroupCardDao(applicationBundle.getJooqBundle().getConfiguration());
    }

    @GET
    public List<GroupCardRes> getGroupCards(@ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser) {
        // TODO access DAO here and get list of group cards
        throw new NotImplementedException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public GroupCardRes createGroupCard(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser,
            GroupCardCreationReq groupCardCreationReq) {
        // TODO put new card in the db
        throw new NotImplementedException();
    }

    @GET
    @Path("/{id}")
    public GroupCardRes getGroupCard(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser,
            @PathParam("id") long personalCardId) {
        // TODO get card from the db
        throw new NotImplementedException();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public GroupCardRes updateGroupCard(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser,
            @PathParam("id") long personalCardId,
            GroupCardUpdateReq groupCardUpdateReq) {
        // TODO update card in the db
        throw new NotImplementedException();
    }

    @DELETE
    @Path("/{id}")
    public GroupCardRes deactivateGroupCard(
            @ApiParam(hidden = true) @Auth AuthorizedUser authorizedUser,
            @PathParam("id") long personalCardId) {
        // TODO deactivate card record
        throw new NotImplementedException();
    }
}
