package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import org.junit.Test;

import java.sql.Date;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by rohan on 5/2/17.
 */
public class CustomCardDaoTest extends AbstractDaoTest {

    Random ran = new Random();

    private static final int TEST_ITERATION = 30;

    @Test
    public void isPersonalTravelCard_returnFalse() {
        IntStream.range(0, TEST_ITERATION).forEach(
                i ->
                        assertFalse(CustomCardDao.isPersonalTravelCard(ran.nextLong(), db))
        );
    }

    @Test
    public void isPersonalTravelCard_returnTrue() {
        createUser("a", Date.valueOf("1234-10-10"), "gay");
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    Long card = createPersonalTravelCardForUser("a");
                    assertTrue(CustomCardDao.isPersonalTravelCard(card, db));
                }
        );
    }

    @Test
    public void isGroupTravelCard_returnFalse() {
        IntStream.range(0, TEST_ITERATION).forEach(
                i ->
                        assertFalse(CustomCardDao.isGroupTravelCard(ran.nextLong(), db))
        );
    }

    @Test
    public void isGroupTravelCard_returnTrue() {
        createUser("a", Date.valueOf("1234-10-10"), "gay");
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    Long card = createGroupTravelCardForUser("a");
                    assertTrue(CustomCardDao.isPersonalTravelCard(card, db));
                }
        );
    }

    @Test
    public void isActiveTravelCard_returnFalse_invalidTravelCard() {
        // case invalid travel card
        IntStream.range(0, TEST_ITERATION).forEach(
                i ->
                        assertFalse(CustomCardDao.isActiveTravelCard(ran.nextLong(), db))
        );
    }

    @Test
    public void isActiveTravelCard_returnFalse_inactiveTravellCard() {
        // case invalid travel card
        createUser("a", Date.valueOf("1999-10-10"), "gay");
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    Long id = createPersonalTravelCardForUser("a");
                    db.update(Tables.CARD)
                            .set(Tables.CARD.ACTIVE, false)
                            .where(Tables.CARD.OWNER_FK.equal("a")).execute();
                    assertFalse(CustomCardDao.isActiveTravelCard(id, db));
                }
        );
    }

    @Test
    public void isActiveTravelCard_returnTrue() {
        // case invalid travel card
        createUser("a", Date.valueOf("1999-10-10"), "gay");
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    Long id = createPersonalTravelCardForUser("a");
                    db.update(Tables.CARD)
                            .set(Tables.CARD.ACTIVE, true)
                            .where(Tables.CARD.OWNER_FK.equal("a")).execute();
                    assertTrue(CustomCardDao.isActiveTravelCard(id, db));
                }
        );
    }

    @Test
    public void isUserAssociatedWithCard_returnFalse_invalidUserName() {
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> assertFalse(CustomCardDao.isUserAssociatedWithCard(
                        String.valueOf(i), ran.nextLong(), db
                ))
        );
    }

    @Test
    public void isUserAssociatedWithCard_returnFalse_invalidCardId() {
        createUser("vic", Date.valueOf("3000-10-10"), "xxx");
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> assertFalse(CustomCardDao.isUserAssociatedWithCard(
                        "vic", ran.nextLong(), db
                ))
        );
    }

    @Test
    public void isUserAssociatedWithCard_returnFalse_notAssociated() {
        final String userName = "vit";
        createUser(userName, Date.valueOf("3000-10-10"), "xxx");
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    Long notAssociated1 = createUserWithGroupTravelCard(String.valueOf(i) + "a");
                    Long notAssociated2 = createUserWithGroupTravelCard(String.valueOf(i) + "b");
                    assertFalse(CustomCardDao.isUserAssociatedWithCard(
                            userName, notAssociated1, db
                    ));
                    assertFalse(CustomCardDao.isUserAssociatedWithCard(
                            userName, notAssociated2, db
                    ));
                }
        );
    }

    @Test
    public void isUserAssociatedWithCard_returnTrue_userAsPeronsalCardOwner() {
        final String userName = "vit";
        createUser(userName, Date.valueOf("3000-10-10"), "xxx");
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    Long associated = createPersonalTravelCardForUser(userName);
                    assertTrue(CustomCardDao.isUserAssociatedWithCard(
                            userName, associated, db
                    ));
                }
        );
    }

    @Test
    public void isUserAssociatedWithCard_returnTrue_userAsGroupCardOwner() {
        final String userName = "vit";
        createUser(userName, Date.valueOf("3000-10-10"), "xxx");
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    Long associated = createGroupTravelCardForUser(userName);
                    assertTrue(CustomCardDao.isUserAssociatedWithCard(
                            userName, associated, db
                    ));
                }
        );
    }

    @Test
    public void isUserAssociatedWithCard_returnTrue_userAsGroupCardParticipant() {
        final String userName = "vit";
        createUser(userName, Date.valueOf("3000-10-10"), "xxx");
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    Long associated = createUserWithGroupTravelCard(String.valueOf(i));
                    addUserToGroupCard(userName, associated);
                    assertTrue(CustomCardDao.isUserAssociatedWithCard(
                            userName, associated, db
                    ));
                }
        );
    }

    @Test
    public void fetchByPosition() {
        // TODO test
    }

    @Test
    public void fetchByUsername() {
        // TODO test
    }
}