package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.daos.TravelerUserDao;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;

import java.util.List;

/**
 * Created by aleksandr on 16.4.2017.
 */
public class GroupCardParticipantDao {

    public List<TravelerUser> getGroupCardParticipants(
            Long cardId,
            DSLContext database,
            TravelerUserDao userDAO
    ) {
        Result<Record1<String>> usernames = database.select(Tables.CARD_USER.USERNAME)
                .from(Tables.CARD_USER)
                .where(Tables.CARD_USER.CARD_ID.equal(cardId))
                .fetch();

        return userDAO.fetchByUsername(usernames.intoArray(Tables.CARD_USER.USERNAME));
    }

    public void addGroupCardParticipant(
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

    public void deleteGroupCardParticipant(
            Long cardId,
            String username,
            DSLContext database
    ) {
        database.deleteFrom(Tables.CARD_USER)
                .where(Tables.CARD_USER.CARD_ID.equal(cardId).and(Tables.CARD_USER.USERNAME.equal(username)))
                .execute();
    }

    public void deleteGroupCardParticipant(
            Long cardId,
            DSLContext database
    ) {
        database.deleteFrom(Tables.CARD_USER)
                .where(Tables.CARD_USER.CARD_ID.equal(cardId))
                .execute();
    }

    public void deleteGroupCardParticipant(
            String username,
            DSLContext database
    ) {
        database.deleteFrom(Tables.CARD_USER)
                .where(Tables.CARD_USER.USERNAME.equal(username))
                .execute();
    }

    public void updateGroupCardParticipants(
            Long cardId,
            List<String> participants,
            DSLContext database
    ) {
        this.deleteGroupCardParticipant(cardId, database);
        participants.stream()
                .forEach(p -> this.addGroupCardParticipant(cardId, p, database));
    }
}
