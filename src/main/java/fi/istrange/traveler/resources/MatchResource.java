package fi.istrange.traveler.resources;

import fi.istrange.traveler.api.MatchForCardRes;
import fi.istrange.traveler.api.MatchResultRes;
import io.dropwizard.auth.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rohan on 4/22/17.
 */
@Path("/match")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/match", tags = "match traveller cards")
public class MatchResource {

    /**
     * Record a decision weather a card like other card.
     * A valid request must provide:
     * <pre>
     *     + valid card ids referring to valid active cards
     *     + two cards must have a shared active period
     *     + two cards must have a certain closeness in location?
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
    @PUT // side-effects of N > 0 identical requests is the same as for a single request
    @Path("/{likerCardId}/{likedCardId}")
    public MatchResultRes match(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("likerCardId") @NotNull Long likerCardId,
            @PathParam("likedCardId") @NotNull Long likedCardId,
            @QueryParam("likeDecision") @NotNull Boolean likeDecision
    ) {
        throw new UnsupportedOperationException("Unimplemented");
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
    @Path("/{cardId}")
    public MatchForCardRes getMatching(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("cardId") @NotNull Long likerCardId
    ) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * Check if two cards matched
     */
    @ApiOperation(value = "Check if there is a match between two cards")
    @GET
    @Path("/{likerCardId}/{likedCardId}")
    public MatchResultRes isAMatch(
            @ApiParam(hidden = true) @Auth DefaultJwtCookiePrincipal principal,
            @PathParam("likerCardId") @NotNull Long likerCardId,
            @PathParam("likedCardId") @NotNull Long likedCardId
    ) {
        throw new UnsupportedOperationException("Unimplemented");
    }
}
