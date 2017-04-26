package fi.istrange.traveler.resources.profile;

import fi.istrange.traveler.api.MatchForCardRes;
import fi.istrange.traveler.api.MatchResultRes;
import fi.istrange.traveler.db.Tables;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.junit.*;

import java.math.BigDecimal;
import java.sql.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static fi.istrange.traveler.db.Tables.*;

/**
 * Created by rohan on 4/25/17.
 */
public class MatchResourceTest extends ResourceTest {

    private final MatchResource matchResource = new MatchResource(appBun);

    private Long createUserWithCard(
            String userName,
            Date birthDate,
            String sex,
            boolean active,
            Date startTime,
            Date endTime,
            BigDecimal lat,
            BigDecimal lon
    ) {
        db.insertInto(
                Tables.TRAVELER_USER,
                TRAVELER_USER.USERNAME, TRAVELER_USER.BIRTH, TRAVELER_USER.GENDER
        ).values(userName, birthDate, sex).execute();

        return db.insertInto(
                Tables.CARD,
                CARD.ACTIVE, CARD.START_TIME, CARD.END_TIME, CARD.LAT, CARD.LON, CARD.OWNER_FK
        ).values(
                active,
                startTime, endTime,
                lat, lon,
                userName
        ).returning(CARD.ID).fetchOne().getValue(CARD.ID);
    }

    private Long createUserWithGroupTravelCard(
            String userName,
            Date birthDate,
            String sex,
            boolean active,
            Date startTime,
            Date endTime,
            BigDecimal lat,
            BigDecimal lon
    ) {
        Long id = createUserWithCard(userName, birthDate, sex, active, startTime, endTime, lat, lon);
        db.insertInto(
                Tables.GROUP_CARD,
                GROUP_CARD.ID
        ).values(id).execute();
        return id;
    }

    private Long createUserWithPersonalTravelCard(
        String userName,
        Date birthDate,
        String sex,
        boolean active,
        Date startTime,
        Date endTime,
        BigDecimal lat,
        BigDecimal lon
    ) {
        Long id = createUserWithCard(userName, birthDate, sex, active, startTime, endTime, lat, lon);
        db.insertInto(
                Tables.PERSONAL_CARD,
                PERSONAL_CARD.ID
        ).values(id).execute();
        return id;
    }

    private Long createUserWithPersonalTravelCard(String userName) {
        return createUserWithPersonalTravelCard(
                userName, Date.valueOf("2017-10-04"), "gay",
                true,
                Date.valueOf("2017-10-05"), Date.valueOf("2017-10-14"),
                BigDecimal.ONE, BigDecimal.TEN
        );
    }

    private Long createUserWithGroupTravelCard(String userName) {
        return createUserWithGroupTravelCard(
                userName, Date.valueOf("2017-10-04"), "gay",
                true,
                Date.valueOf("2017-10-05"), Date.valueOf("2017-10-14"),
                BigDecimal.ONE, BigDecimal.TEN
        );
    }

    @Test
    public void getMatching_Empty_PersonalTravelCard() throws Exception {
        long card1 = createUserWithPersonalTravelCard("a");

        final MatchForCardRes res = matchResource.getMatching(new DefaultJwtCookiePrincipal("a"), card1, db);

        assertTrue(res.getMatchedGroupCard().isEmpty());
        assertTrue(res.getMatchedPersonalCard().isEmpty());
    }

    @Test
    public void getMatching_Empty_GroupTravelCard() throws Exception {
        long card1 = createUserWithGroupTravelCard("a");

        final MatchForCardRes res = matchResource.getMatching(new DefaultJwtCookiePrincipal("a"), card1, db);

        assertTrue(res.getMatchedGroupCard().isEmpty());
        assertTrue(res.getMatchedPersonalCard().isEmpty());
    }

    @Test
    public void dislike() throws Exception {
        long card1 = createUserWithPersonalTravelCard("a");
        long card2 = createUserWithPersonalTravelCard("b");
        MatchResultRes res1 = matchResource.like(
                new DefaultJwtCookiePrincipal("a"),
                card1, card2, db);
        assertFalse(res1.isAMatch());
    }

    @Test
    public void like() throws Exception {
        long card1 = createUserWithPersonalTravelCard("a");
        long card2 = createUserWithPersonalTravelCard("b");
        MatchResultRes res1 = matchResource.like(
                new DefaultJwtCookiePrincipal("a"),
                card1, card2, db);
        assertFalse(res1.isAMatch());

        MatchResultRes res2 = matchResource.like(
                new DefaultJwtCookiePrincipal("b"),
                card2, card1, db);
        assertTrue(res2.isAMatch());

        MatchForCardRes matching1 = matchResource.getMatching(
                new DefaultJwtCookiePrincipal("a"),
                card1, db
        );
        assertTrue(matching1.getMatchedGroupCard().isEmpty());
        assertTrue(matching1.getMatchedPersonalCard().size() == 1);
        assertTrue(matching1.getMatchedPersonalCard().get(0).getOwner().getUsername().equals("b"));

        MatchForCardRes matching2 = matchResource.getMatching(
                new DefaultJwtCookiePrincipal("b"),
                card2, db
        );
        assertTrue(matching2.getMatchedGroupCard().isEmpty());
        assertTrue(matching2.getMatchedPersonalCard().size() == 1);
        assertTrue(matching2.getMatchedPersonalCard().get(0).getOwner().getUsername().equals("a"));


    }

    @Test
    public void isAMatch() throws Exception {
        long card1 = createUserWithPersonalTravelCard("a");
        long card2 = createUserWithPersonalTravelCard("b");
        matchResource.like(
                new DefaultJwtCookiePrincipal("a"),
                card1, card2, db);
        matchResource.like(
                new DefaultJwtCookiePrincipal("b"),
                card2, card1, db);
        MatchResultRes a_match_b = matchResource.isAMatch(
                new DefaultJwtCookiePrincipal("a"),
                card1, card2, db
        );
        assertTrue(a_match_b.isAMatch());
        MatchResultRes b_match_a = matchResource.isAMatch(
                new DefaultJwtCookiePrincipal("b"),
                card2, card1, db
        );
        assertTrue(b_match_a.isAMatch());
    }
}