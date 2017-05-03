package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static fi.istrange.traveler.dao.MatchCustomDao.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by rohan on 5/2/17.
 */
public class MatchCustomDaoTest extends AbstractDaoTest {

    private String userName1 = "a";
    private String userName2 = "b";

    @Test
    public void createOrUpdateMatch_personal_personal() {
        Long card1 = createUserWithPersonalTravelCard(userName1);
        Long card2 = createUserWithPersonalTravelCard(userName2);
        createOrUpdateLike(card1, card2, false, db);
        assertTrue(db.fetchExists(Tables.MATCH));
    }
    @Test
    public void createOrUpdateMatch_personal_group() {
        Long card1 = createUserWithPersonalTravelCard(userName1);
        Long card2 = createUserWithGroupTravelCard(userName2);
        createOrUpdateLike(card1, card2, false, db);
        assertTrue(db.fetchExists(Tables.MATCH));
    }
    @Test
    public void createOrUpdateMatch_group_personal() {
        Long card1 = createUserWithGroupTravelCard(userName1);
        Long card2 = createUserWithPersonalTravelCard(userName2);
        createOrUpdateLike(card1, card2, false, db);
        assertTrue(db.fetchExists(Tables.MATCH));
    }
    @Test
    public void createOrUpdateMatch_group_group() {
        Long card1 = createUserWithGroupTravelCard(userName1);
        Long card2 = createUserWithGroupTravelCard(userName2);
        createOrUpdateLike(card1, card2, false, db);
        assertTrue(db.fetchExists(Tables.MATCH));
    }

    private void testMatchTwoCard(Long card1, Long card2) {
        createOrUpdateLike(card1, card2, true, db);
        createOrUpdateLike(card2, card1, true, db);
        assertTrue(isMatch(card1, card2, db));
    }

    @Test
    public void isMatchTrue_personal_personal() {
        Long card1 = createUserWithPersonalTravelCard(userName1);
        Long card2 = createUserWithPersonalTravelCard(userName2);
        testMatchTwoCard(card1, card2);
    }

    @Test
    public void isMatchTrue_personal_group() {
        Long card1 = createUserWithPersonalTravelCard(userName1);
        Long card2 = createUserWithGroupTravelCard(userName2);
        testMatchTwoCard(card1, card2);
    }
    @Test
    public void isMatchTrue_group_personal() {
        Long card1 = createUserWithPersonalTravelCard(userName1);
        Long card2 = createUserWithGroupTravelCard(userName2);
        testMatchTwoCard(card1, card2);
    }
    @Test
    public void isMatchTrue_group_group() {
        Long card1 = createUserWithGroupTravelCard(userName1);
        Long card2 = createUserWithGroupTravelCard(userName2);
        testMatchTwoCard(card1, card2);
    }

    private void testMismatchTowCard(Long card1, Long card2) {
        createOrUpdateLike(card1, card2, false, db);
        assertFalse(isMatch(card1, card2, db));
        assertFalse(isMatch(card2, card1, db));
        createOrUpdateLike(card2, card1, true, db);
        assertFalse(isMatch(card1, card2, db));
        assertFalse(isMatch(card2, card1, db));
        createOrUpdateLike(card2, card1, false, db);
        assertFalse(isMatch(card1, card2, db));
        assertFalse(isMatch(card2, card1, db));
        createOrUpdateLike(card1, card2, true, db);
        assertFalse(isMatch(card1, card2, db));
        assertFalse(isMatch(card2, card1, db));
    }

    @Test
    public void isMatchFalse_personal_personal() {
        Long card1 = createUserWithPersonalTravelCard(userName1);
        Long card2 = createUserWithPersonalTravelCard(userName2);
        testMismatchTowCard(card1, card2);
    }

    @Test
    public void isMatchFalse_personal_group() {
        Long card1 = createUserWithPersonalTravelCard(userName1);
        Long card2 = createUserWithGroupTravelCard(userName2);
        testMismatchTowCard(card1, card2);
    }
    @Test
    public void isMatchFalse_group_personal() {
        Long card1 = createUserWithPersonalTravelCard(userName1);
        Long card2 = createUserWithGroupTravelCard(userName2);
        testMismatchTowCard(card1, card2);
    }
    @Test
    public void isMatchFalse_group_group() {
        Long card1 = createUserWithGroupTravelCard(userName1);
        Long card2 = createUserWithGroupTravelCard(userName2);
        testMismatchTowCard(card1, card2);
    }

    private Random ran = new Random();
    private final int TEST_ITERATION = 2;

    private Set<Long> createCard(Long matched, Function<String, Long> register) {
        Set<Long> expectedId = new HashSet<>();
        Set<String> usedUsername = new HashSet<>();
        for(int i = 0; i < TEST_ITERATION; i++) {
            String userName = String.valueOf(ran.nextInt());
            if(!usedUsername.contains(userName)) {
                usedUsername.add(userName);
                Long id = register.apply(userName);
                testMatchTwoCard(matched, id);
                expectedId.add(id);
            }
        }
        return expectedId;
    }

    @Test
    public void getMatchingFor_personal_matchAllPersonal() {
        Long cardId = createUserWithPersonalTravelCard(userName1);
        Set<Long> expectedId = createCard(cardId, this::createUserWithGroupTravelCard);
        assertTrue(new HashSet<>(getMatchingFor(cardId, db)).equals(expectedId));
    }

    @Test
    public void getMatchingFor_group_matchAllPersonal() {
        Long cardId = createUserWithGroupTravelCard(userName1);
        Set<Long> expectedId = createCard(cardId, this::createUserWithPersonalTravelCard);
        assertTrue(new HashSet<>(getMatchingFor(cardId, db)).equals(expectedId));
    }


    @Test
    public void getMatchingFor_personal_matchAllGroup() {
        Long cardId = createUserWithPersonalTravelCard(userName1);
        Set<Long> expectedId = createCard(cardId, this::createUserWithGroupTravelCard);
        assertTrue(new HashSet<>(getMatchingFor(cardId, db)).equals(expectedId));
    }

    @Test
    public void getMatchingFor_group_matchAllGroup() {
        Long cardId = createUserWithGroupTravelCard(userName1);
        Set<Long> expectedId = createCard(cardId, this::createUserWithPersonalTravelCard);
        assertTrue(new HashSet<>(getMatchingFor(cardId, db)).equals(expectedId));
    }

    @Test
    public void getMatchingFor_personal_matchPersonalAndGroup() {
        Long cardId = createUserWithPersonalTravelCard(userName1);
        Set<Long> expectedPersonalId = createCard(cardId, this::createUserWithPersonalTravelCard);
        assertTrue(new HashSet<>(getMatchingFor(cardId, db)).equals(expectedPersonalId));
        Set<Long> expectedGroupId = createCard(cardId, this::createUserWithGroupTravelCard);
        expectedGroupId.addAll(expectedPersonalId);
        assertTrue(new HashSet<>(getMatchingFor(cardId, db)).equals(expectedGroupId));
    }

    @Test
    public void getMatchingFor_group_matchPersonalAndGroup() {
        Long cardId = createUserWithGroupTravelCard(userName1);
        Set<Long> expectedPersonalId = createCard(cardId, this::createUserWithPersonalTravelCard);
        assertTrue(new HashSet<>(getMatchingFor(cardId, db)).equals(expectedPersonalId));
        Set<Long> expectedGroupId = createCard(cardId, this::createUserWithGroupTravelCard);
        expectedGroupId.addAll(expectedPersonalId);
        assertTrue(new HashSet<>(getMatchingFor(cardId, db)).equals(expectedGroupId));
    }
}