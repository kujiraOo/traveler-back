package fi.istrange.traveler.dao;

/**
 * Created by rohan on 4/23/17.
 */

import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.pojos.Card;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * This class host convenient method for travel card as a whole
 */
public class CustomCardDao {
    public static boolean isPersonalTravelCard(Long cardId) {
        Objects.requireNonNull(cardId);
        // even
        return cardId % 2 == 0;
    }

    public static boolean isGroupTravelCard(Long cardId) {
        Objects.requireNonNull(cardId);
        // odd
        return cardId % 2 == 1;
    }

    /**
     * Inspect weather a card is valid, active travel card.
     * The card may be a group travel card or personal travel cards.
     */
    public static boolean isActiveTravelCard(Long cardId, DSLContext db) {
        Objects.requireNonNull(cardId);

        return db.fetchExists(
                Tables.CARD,
                Tables.CARD.ID.equal(cardId)
                        .and(Tables.CARD.ACTIVE.equal(true))
        );
    }

    /**
     * Inspect weather an user is associated with a travel card
     * An user is associated with a travel card iff
     * <pre>
     *     + case PersonalTravel card: the person is card's creator
     *     + case GroupTravel card: the person is either card's creator
     *     or card's participants
     * </pre>
     * @param userName username that may not have been registered
     * @param cardId cardId that may not have been registered
     * @param db database
     * @return true iff given valid userName, cardId, and user identified by userName
     * is associated with the card
     */
    public static boolean isUserAssociatedWithCard(String userName, Long cardId, DSLContext db) {
        Objects.requireNonNull(cardId);
        Objects.requireNonNull(userName);

        final boolean userAsCreator = db.fetchExists(
                Tables.CARD,
                Tables.CARD.ID.equal(cardId)
                        .and(Tables.CARD.OWNER_FK.equal(userName))
        );

        if (isPersonalTravelCard(cardId)) {
            return userAsCreator;
        } else if (isGroupTravelCard(cardId)) {
            if (userAsCreator) return true;
            final boolean userAsParticipant = db.fetchExists(
                    Tables.CARD_USER,
                    Tables.CARD_USER.CARD_ID.equal(cardId)
                            .and(Tables.CARD_USER.USERNAME.equal(userName))
            );
            return userAsParticipant;
        } else {
            throw new RuntimeException("Should not be here");
        }

    }

    public List<Card> fetchByPosition(
            CardType type,
            BigDecimal lat,
            BigDecimal lon,
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        // TODO: change 1 to the value that atually makes sense
        return fetch(
                type,
                Tables.CARD.LAT.between(
                        lat.subtract(new BigDecimal(1)),
                        lat.add(new BigDecimal(1))
                )
                        .and(Tables.CARD.LON.between(
                                lon.subtract(new BigDecimal(1)),
                                lon.add(new BigDecimal(1)))
                        ),
                fetchArchived,
                offset,
                database
        );
    }

    public List<Card> fetchByUsername(
            CardType type,
            String username,
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        return fetch(
                type,
                Tables.CARD.OWNER_FK.equal(username),
                fetchArchived,
                offset,
                database
        );
    }

    public List<Card> fetchAll(
            CardType type,
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        return fetch(
                type,
                Tables.CARD.ID.greaterOrEqual(0L), // workaround because not able to pass boolean
                fetchArchived,
                offset,
                database
        );
    }


    public List<Card> fetch(
            CardType type,
            Condition condition,
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        Table table = (type.equals(CardType.PERSONAL) ? Tables.PERSONAL_CARD : Tables.GROUP_CARD);
        Field joinField = (type.equals(CardType.PERSONAL) ? Tables.PERSONAL_CARD.ID : Tables.GROUP_CARD.ID);

        return database.select()
                .from(table.join(Tables.CARD).on(joinField.equal(Tables.CARD.ID)))
                .where(
                        condition
                                .and(Tables.CARD.ACTIVE.equal(true)
                                        .or(Tables.CARD.ACTIVE.equal(!fetchArchived)))
                )
                .offset(offset)
                .limit(20)
                .fetch()
                .map(p -> new Card(
                        p.getValue(Tables.CARD.ID),
                        p.getValue(Tables.CARD.START_TIME),
                        p.getValue(Tables.CARD.END_TIME),
                        p.getValue(Tables.CARD.LON),
                        p.getValue(Tables.CARD.LAT),
                        p.getValue(Tables.CARD.OWNER_FK),
                        p.getValue(Tables.CARD.ACTIVE)
                ));
    }
}
