package fi.istrange.traveler.resources.profile;

import fi.istrange.traveler.api.MatchForCardRes;
import fi.istrange.traveler.api.MatchResultRes;
import fi.istrange.traveler.resources.AbstractResourceTest;
import org.dhatim.dropwizard.jwt.cookie.authentication.DefaultJwtCookiePrincipal;
import org.junit.*;

import javax.ws.rs.BadRequestException;
import java.sql.Date;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by rohan on 4/25/17.
 */
public class MatchResourceTest extends AbstractResourceTest {

    private final MatchResource matchResource = new MatchResource(appBun);
    private static final String user01 = "a";
    private static final String user02 = "b";
    private static final int TEST_ITERATION = 2;

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


    // like:
    // + liker: personal, group card
    // + liked: personal, group card
    @Test
    public void like_personal_personal() throws Exception {
        final String card1_user = "a";
        final String card2_user = "b";
        long card1 = createUserWithPersonalTravelCard(card1_user);
        long card2 = createUserWithPersonalTravelCard(card2_user);
        MatchResultRes res1 = matchResource.like(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, card2, db);
        assertFalse(res1.isAMatch());

        MatchResultRes res2 = matchResource.like(
                new DefaultJwtCookiePrincipal(card2_user),
                card2, card1, db);
        assertTrue(res2.isAMatch());

        MatchForCardRes matching1 = matchResource.getMatching(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, db
        );
        assertTrue(matching1.getMatchedGroupCard().isEmpty());
        assertTrue(matching1.getMatchedPersonalCard().size() == 1);
        assertTrue(matching1.getMatchedPersonalCard().get(0).getOwner().getUsername().equals(card2_user));

        MatchForCardRes matching2 = matchResource.getMatching(
                new DefaultJwtCookiePrincipal(card2_user),
                card2, db
        );
        assertTrue(matching2.getMatchedGroupCard().isEmpty());
        assertTrue(matching2.getMatchedPersonalCard().size() == 1);
        assertTrue(matching2.getMatchedPersonalCard().get(0).getOwner().getUsername().equals(card1_user));
    }

    @Test
    public void like_personal_group() throws Exception {
        final String card1_user = "a";
        final String card2_user = "b";
        long card1 = createUserWithPersonalTravelCard(card1_user);
        long card2 = createUserWithGroupTravelCard(card2_user);

        MatchResultRes res1 = matchResource.like(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, card2, db);
        assertFalse(res1.isAMatch());

        MatchResultRes res2 = matchResource.like(
                new DefaultJwtCookiePrincipal(card2_user),
                card2, card1, db);
        assertTrue(res2.isAMatch());

        MatchForCardRes matching1 = matchResource.getMatching(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, db
        );

        assertTrue(matching1.getMatchedGroupCard().size() == 1);
        assertTrue(matching1.getMatchedPersonalCard().isEmpty());
        assertTrue(matching1.getMatchedGroupCard().get(0).getOwner().getUsername().equals(card2_user));

        MatchForCardRes matching2 = matchResource.getMatching(
                new DefaultJwtCookiePrincipal(card2_user),
                card2, db
        );
        assertTrue(matching2.getMatchedGroupCard().isEmpty());
        assertTrue(matching2.getMatchedPersonalCard().size() == 1);
        assertTrue(matching2.getMatchedPersonalCard().get(0).getOwner().getUsername().equals(card1_user));
    }

    @Test
    public void like_group_personal() throws Exception {
        final String card1_user = "a";
        final String card2_user = "b";
        long card1 = createUserWithGroupTravelCard(card1_user);
        long card2 = createUserWithPersonalTravelCard(card2_user);

        MatchResultRes res1 = matchResource.like(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, card2, db);
        assertFalse(res1.isAMatch());

        MatchResultRes res2 = matchResource.like(
                new DefaultJwtCookiePrincipal(card2_user),
                card2, card1, db);
        assertTrue(res2.isAMatch());

        MatchForCardRes matching2 = matchResource.getMatching(
                new DefaultJwtCookiePrincipal(card2_user),
                card2, db
        );
        assertTrue(matching2.getMatchedGroupCard().size() == 1);
        assertTrue(matching2.getMatchedPersonalCard().isEmpty());
        assertTrue(matching2.getMatchedGroupCard().get(0).getOwner().getUsername().equals(card1_user));

        MatchForCardRes matching1 = matchResource.getMatching(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, db
        );
        assertTrue(matching1.getMatchedGroupCard().isEmpty());
        assertTrue(matching1.getMatchedPersonalCard().size() == 1);
        assertTrue(matching1.getMatchedPersonalCard().get(0).getOwner().getUsername().equals(card2_user));
    }

    @Test
    public void like_group_group() throws Exception {
        final String card1_user = "a";
        final String card2_user = "b";
        long card1 = createUserWithGroupTravelCard(card1_user);
        long card2 = createUserWithGroupTravelCard(card2_user);

        MatchResultRes res1 = matchResource.like(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, card2, db);
        assertFalse(res1.isAMatch());

        MatchResultRes res2 = matchResource.like(
                new DefaultJwtCookiePrincipal(card2_user),
                card2, card1, db);
        assertTrue(res2.isAMatch());

        MatchForCardRes matching1 = matchResource.getMatching(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, db
        );
        assertTrue(matching1.getMatchedGroupCard().size() == 1);
        assertTrue(matching1.getMatchedPersonalCard().isEmpty());
        assertTrue(matching1.getMatchedGroupCard().get(0).getOwner().getUsername().equals(card2_user));

        MatchForCardRes matching2 = matchResource.getMatching(
                new DefaultJwtCookiePrincipal(card2_user),
                card2, db
        );
        assertTrue(matching2.getMatchedGroupCard().size() == 1);
        assertTrue(matching2.getMatchedPersonalCard().isEmpty());
        assertTrue(matching2.getMatchedGroupCard().get(0).getOwner().getUsername().equals(card1_user));
    }

    // test isAMatch

    private void testMatch(String user1, Long user1Card, String user2, Long user2Card) {
        matchResource.like(
                new DefaultJwtCookiePrincipal(user1),
                user1Card, user2Card, db);
        matchResource.like(
                new DefaultJwtCookiePrincipal(user2),
                user2Card, user1Card, db);
        MatchResultRes aMatchB = matchResource.isAMatch(
                new DefaultJwtCookiePrincipal(user01),
                user1Card, user2Card, db
        );
        assertTrue(aMatchB.isAMatch());
        MatchResultRes bMatchA = matchResource.isAMatch(
                new DefaultJwtCookiePrincipal(user02),
                user2Card, user1Card, db
        );
        assertTrue(bMatchA.isAMatch());
    }

    @Test
    public void isAMatch_personal_personal() throws Exception {
        long card1 = createUserWithPersonalTravelCard(user01);
        long card2 = createUserWithPersonalTravelCard(user02);
        testMatch(user01, card1, user02, card2);
    }

    @Test
    public void isAMatch_personal_group() throws Exception {
        long card1 = createUserWithPersonalTravelCard(user01);
        long card2 = createUserWithGroupTravelCard(user02);
        testMatch(user01, card1, user02, card2);
    }

    @Test
    public void isAMatch_group_personal() throws Exception {
        long card1 = createUserWithGroupTravelCard(user01);
        long card2 = createUserWithPersonalTravelCard(user02);
        testMatch(user01, card1, user02, card2);
    }

    @Test
    public void isAMatch_group_personal_likerAsParticipant() throws Exception {
        long card1 = createUserWithGroupTravelCard(user01);
        final String participant = "participant";
        createUser(participant, Date.valueOf("2019-10-10"), "gay");
        addUserToGroupCard(participant, card1);
        long card2 = createUserWithPersonalTravelCard(user02);
        testMatch(participant, card1, user02, card2);
    }

    @Test
    public void isAMatch_group_group() throws Exception {
        long card1 = createUserWithGroupTravelCard(user01);
        long card2 = createUserWithGroupTravelCard(user02);
        testMatch(user01, card1, user02, card2);
    }

    @Test
    public void isAMatch_group_group_liker01AsParticipant() throws Exception {
        long card1 = createUserWithGroupTravelCard(user01);
        final String liker01 = "participant";
        createUser(liker01, Date.valueOf("2019-10-10"), "gay");
        addUserToGroupCard(liker01, card1);
        long card2 = createUserWithGroupTravelCard(user02);
        testMatch(liker01, card1, user02, card2);
    }


    @Test
    public void isAMatch_group_group_bothLikerAsParticipant() throws Exception {
        long card1 = createUserWithGroupTravelCard(user01);
        final String liker01 = "liker01";
        createUser(liker01, Date.valueOf("2019-10-10"), "gay");
        addUserToGroupCard(liker01, card1);
        long card2 = createUserWithGroupTravelCard(user02);
        final String liker02 = "liker02";
        createUser(liker02, Date.valueOf("2019-10-10"), "gay");
        addUserToGroupCard(liker02, card2);
        testMatch(liker01, card1, liker02, card2);
    }

    // test throwing BadRequestException
    @Test
    public void invalidLikerCardId() {
        long card1 = createUserWithPersonalTravelCard(user01);
        long card2 = createUserWithPersonalTravelCard(user02);
        int exceptionCount = 0;
        int expected = TEST_ITERATION;
        Random ran = new Random();
        for (int i = 0; i < TEST_ITERATION; i++) {
            long invalidCardId = ran.nextLong();
            if (invalidCardId == card1) return;
            try {
                matchResource.like(
                        new DefaultJwtCookiePrincipal(user01),
                        invalidCardId, card2, db);
            } catch (BadRequestException ex) {
                exceptionCount ++;
            }
        }
        assertTrue( exceptionCount == expected);
    }

    @Test
    public void invalidLikedCardId() {
        long card1 = createUserWithPersonalTravelCard(user01);
        long card2 = createUserWithPersonalTravelCard(user02);
        int expected = TEST_ITERATION;
        int exceptionCount = 0;
        Random ran = new Random();
        for (int i = 0; i < TEST_ITERATION; i++) {
            long invalidCardId = ran.nextLong();
            if(invalidCardId == card2) {
                return;
            }
            try {
                matchResource.like(
                        new DefaultJwtCookiePrincipal(user01),
                        card1, invalidCardId, db);
            } catch (BadRequestException ex) {
                exceptionCount ++;
            }
        }
        assertTrue( exceptionCount == expected);
    }

    @Test(expected = BadRequestException.class)
    public void likedPersonalCard_associatedWithLiker_01() {
        long card1 = createUserWithPersonalTravelCard(user01);
        long card2 = createPersonalTravelCardForUser(user01);
        matchResource.like(
                new DefaultJwtCookiePrincipal(user01),
                card1, card2, db
        );
    }

    @Test(expected = BadRequestException.class)
    public void likedPersonalCard_associatedWithLiker_02() {
        long card1 = createUserWithGroupTravelCard(user01);
        long card2 = createPersonalTravelCardForUser(user01);
        matchResource.like(
                new DefaultJwtCookiePrincipal(user01),
                card1, card2, db
        );
    }

    @Test(expected = BadRequestException.class)
    public void likedGroupCard_associatedWithLiker_01() {
        long card1 = createUserWithPersonalTravelCard(user01);
        long card2 = createGroupTravelCardForUser(user01);
        matchResource.like(
                new DefaultJwtCookiePrincipal(user01),
                card1, card2, db
        );
    }

    @Test(expected = BadRequestException.class)
    public void likedGroupCard_associatedWithLiker_02() {
        long card1 = createUserWithGroupTravelCard(user01);
        long card2 = createGroupTravelCardForUser(user01);
        matchResource.like(
                new DefaultJwtCookiePrincipal(user01),
                card1, card2, db
        );
    }

    @Test(expected = BadRequestException.class)
    public void likedGroupCard_associatedWithLiker_03() {
        // user as participant
        long card1 = createUserWithPersonalTravelCard(user01);
        long card2 = createUserWithGroupTravelCard(user02);
        addUserToGroupCard(user01, card2);
        matchResource.like(
                new DefaultJwtCookiePrincipal(user01),
                card1, card2, db
        );
    }

    @Test(expected = BadRequestException.class)
    public void likedGroupCard_associatedWithLiker_04() {
        // user as participant
        long card1 = createUserWithGroupTravelCard(user01);
        long card2 = createUserWithGroupTravelCard(user02);
        addUserToGroupCard(user01, card2);
        matchResource.like(
                new DefaultJwtCookiePrincipal(user01),
                card1, card2, db
        );
    }
}