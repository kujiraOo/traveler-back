package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;
import org.jooq.DSLContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by aleksandr on 16.4.2017.
 */
public class GroupCardParticipantDao {

    public static Set<TravelerUser> getGroupCardParticipants(
            Long cardId,
            DSLContext database,
            TravelerUserDao userDAO
    ) {
        List<String> usernames = database
                .select()
                .from(Tables.CARD_USER)
                .where(Tables.CARD_USER.CARD_ID.equal(cardId))
                .fetch(Tables.CARD_USER.USERNAME);

        return new HashSet<>(userDAO.fetchByUsername(usernames.toArray(new String[0])));
    }

    public static void addGroupCardParticipant(
            Long cardId,
            String username,
            DSLContext database
    ) {
        database.insertInto(
                Tables.CARD_USER,
                Tables.CARD_USER.USERNAME,
                Tables.CARD_USER.CARD_ID
            ).values(username, cardId)
            .execute();
    }

    public static void deleteGroupCardParticipant(
            Long cardId,
            DSLContext database
    ) {
        database.deleteFrom(Tables.CARD_USER)
                .where(Tables.CARD_USER.CARD_ID.equal(cardId))
                .execute();
    }

    public static void updateGroupCardParticipants(
            Long cardId,
            List<String> participants,
            DSLContext database
    ) {
        deleteGroupCardParticipant(cardId, database);
        participants.stream()
                .forEach(p -> addGroupCardParticipant(cardId, p, database));
    }
}
