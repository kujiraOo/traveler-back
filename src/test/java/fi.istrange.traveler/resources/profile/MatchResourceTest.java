package fi.istrange.traveler.resources.profile;

import fi.istrange.traveler.api.MatchForCardRes;
import fi.istrange.traveler.api.MatchResultRes;
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
public class MatchResourceTest extends ResourceTest {

    private final MatchResource matchResource = new MatchResource(appBun);

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
        MatchResultRes a_match_b = matchResource.isAMatch(
                new DefaultJwtCookiePrincipal(card1_user),
                user1Card, user2Card, db
        );
        assertTrue(a_match_b.isAMatch());
        MatchResultRes b_match_a = matchResource.isAMatch(
                new DefaultJwtCookiePrincipal(card2_user),
                user2Card, user1Card, db
        );
        assertTrue(b_match_a.isAMatch());
    }

    @Test
    public void isAMatch_personal_personal() throws Exception {
        long card1 = createUserWithPersonalTravelCard(card1_user);
        long card2 = createUserWithPersonalTravelCard(card2_user);
        testMatch(card1_user, card1, card2_user, card2);
    }

    @Test
    public void isAMatch_personal_group() throws Exception {
        long card1 = createUserWithPersonalTravelCard(card1_user);
        long card2 = createUserWithGroupTravelCard(card2_user);
        testMatch(card1_user, card1, card2_user, card2);
    }

    @Test
    public void isAMatch_group_personal() throws Exception {
        long card1 = createUserWithGroupTravelCard(card1_user);
        long card2 = createUserWithPersonalTravelCard(card2_user);
        testMatch(card1_user, card1, card2_user, card2);
    }

    @Test
    public void isAMatch_group_personal_likerAsParticipant() throws Exception {
        long card1 = createUserWithGroupTravelCard(card1_user);
        final String participant = "participant";
        createUser(participant, Date.valueOf("2019-10-10"), "gay");
        addUserToGroupCard(participant, card1);
        long card2 = createUserWithPersonalTravelCard(card2_user);
        testMatch(participant, card1, card2_user, card2);
    }

    @Test
    public void isAMatch_group_group() throws Exception {
        long card1 = createUserWithGroupTravelCard(card1_user);
        long card2 = createUserWithGroupTravelCard(card2_user);
        testMatch(card1_user, card1, card2_user, card2);
    }

    @Test
    public void isAMatch_group_group_liker01AsParticipant() throws Exception {
        long card1 = createUserWithGroupTravelCard(card1_user);
        final String liker01 = "participant";
        createUser(liker01, Date.valueOf("2019-10-10"), "gay");
        addUserToGroupCard(liker01, card1);
        long card2 = createUserWithGroupTravelCard(card2_user);
        testMatch(liker01, card1, card2_user, card2);
    }


    @Test
    public void isAMatch_group_group_bothLikerAsParticipant() throws Exception {
        long card1 = createUserWithGroupTravelCard(card1_user);
        final String liker01 = "liker01";
        createUser(liker01, Date.valueOf("2019-10-10"), "gay");
        addUserToGroupCard(liker01, card1);
        long card2 = createUserWithGroupTravelCard(card2_user);
        final String liker02 = "liker02";
        createUser(liker02, Date.valueOf("2019-10-10"), "gay");
        addUserToGroupCard(liker02, card2);
        testMatch(liker01, card1, liker02, card2);
    }

    private static final String card1_user = "a";
    private static final String card2_user = "b";


    // test throwing BadRequestException
    @Test
    public void invalidLikerCardId() {
        long card1 = createUserWithPersonalTravelCard(card1_user);
        long card2 = createUserWithPersonalTravelCard(card2_user);
        int exception_count = 0;
        int expected = 1000;
        Random ran = new Random();
        for (int i = 0; i < 1000; i++) {
            long invalid_card1_id = ran.nextLong();
            if (invalid_card1_id == card1) return;
            try {
                matchResource.like(
                        new DefaultJwtCookiePrincipal(card1_user),
                        invalid_card1_id, card2, db);
            } catch (BadRequestException ex) {
                exception_count ++;
            }
        }
        assertTrue( exception_count == expected);
    }

    @Test
    public void invalidLikedCardId() {
        long card1 = createUserWithPersonalTravelCard(card1_user);
        long card2 = createUserWithPersonalTravelCard(card2_user);
        int expected = 1000;
        int exception_count = 0;
        Random ran = new Random();
        for (int i = 0; i < 1000; i++) {
            long invalid_card2_id = ran.nextLong();
            if(invalid_card2_id == card2) {
                return;
            }
            try {
                matchResource.like(
                        new DefaultJwtCookiePrincipal(card1_user),
                        card1, invalid_card2_id, db);
            } catch (BadRequestException ex) {
                exception_count ++;
            }
        }
        assertTrue( exception_count == expected);
    }

    @Test(expected = BadRequestException.class)
    public void likedPersonalCard_associatedWithLiker_01() {
        long card1 = createUserWithPersonalTravelCard(card1_user);
        long card2 = createPersonalTravelCardForUser(card1_user);
        matchResource.like(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, card2, db
        );
    }

    @Test(expected = BadRequestException.class)
    public void likedPersonalCard_associatedWithLiker_02() {
        long card1 = createUserWithGroupTravelCard(card1_user);
        long card2 = createPersonalTravelCardForUser(card1_user);
        matchResource.like(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, card2, db
        );
    }

    @Test(expected = BadRequestException.class)
    public void likedGroupCard_associatedWithLiker_01() {
        long card1 = createUserWithPersonalTravelCard(card1_user);
        long card2 = createGroupTravelCardForUser(card1_user);
        matchResource.like(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, card2, db
        );
    }

    @Test(expected = BadRequestException.class)
    public void likedGroupCard_associatedWithLiker_02() {
        long card1 = createUserWithGroupTravelCard(card1_user);
        long card2 = createGroupTravelCardForUser(card1_user);
        matchResource.like(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, card2, db
        );
    }

    @Test(expected = BadRequestException.class)
    public void likedGroupCard_associatedWithLiker_03() {
        // user as participant
        long card1 = createUserWithPersonalTravelCard(card1_user);
        long card2 = createUserWithGroupTravelCard(card2_user);
        addUserToGroupCard(card1_user, card2);
        matchResource.like(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, card2, db
        );
    }

    @Test(expected = BadRequestException.class)
    public void likedGroupCard_associatedWithLiker_04() {
        // user as participant
        long card1 = createUserWithGroupTravelCard(card1_user);
        long card2 = createUserWithGroupTravelCard(card2_user);
        addUserToGroupCard(card1_user, card2);
        matchResource.like(
                new DefaultJwtCookiePrincipal(card1_user),
                card1, card2, db
        );
    }
}