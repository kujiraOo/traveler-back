package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;
import org.junit.Test;

import java.sql.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static fi.istrange.traveler.dao.GroupCardParticipantDao.*;

import static org.junit.Assert.*;

/**
 * Created by rohan on 5/2/17.
 */
public class GroupCardParticipantDaoTest extends AbstractDaoTest {

    private TravelerUserDao travelerUserDao = new TravelerUserDao(configuration);
    private Random ran = new Random();
    private final int TEST_ITERATION = 2;

    @Test
    public void getGroupCardParticipantss_invalidCardId() {
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    Set<TravelerUser> list = getGroupCardParticipants(
                            ran.nextLong(), db, travelerUserDao
                    );
                    assertTrue(list.isEmpty());
                });
    }

    @Test
    public void getGroupCardParticipantss() {
        Long groupCard = createUserWithGroupTravelCard("vit");
        Set<String> participants = new HashSet<>();

        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    String participant = String.valueOf(i);
                    createUser(participant, Date.valueOf("2000-10-10"), "gay");
                    addUserToGroupCard(participant, groupCard);
                    participants.add(participant);
                });
        assertTrue(participants.equals(
                getGroupCardParticipants(groupCard, db, travelerUserDao)
                    .stream().map( travelerUser -> travelerUser.getUsername())
                    .collect(Collectors.toSet())
        ));
    }

    @Test
    public void addGroupCardParticipant() {
        Long groupCard = createUserWithGroupTravelCard("vit");
        Set<String> participants = new HashSet<>();
        IntStream.range(0, TEST_ITERATION).forEach(
                i -> {
                    String participant = String.valueOf(i);
                    createUser(participant, Date.valueOf("2000-10-10"), "gay");
                    GroupCardParticipantDao.addGroupCardParticipant(groupCard, participant, db);
                    participants.add(participant);
                });
        assertTrue(participants.equals(
                getGroupCardParticipants(groupCard, db, travelerUserDao)
                        .stream().map( travelerUser -> travelerUser.getUsername())
                        .collect(Collectors.toSet())
        ));
    }

    @Test
    public void deleteGroupCardParticipant() {
        assertTrue(true);
    }

    @Test
    public void updateGroupCardParticipants() {
        assertTrue(true);
    }
}