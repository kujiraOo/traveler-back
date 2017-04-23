package fi.istrange.traveler.dao;

/**
 * Created by rohan on 4/23/17.
 */

import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;
import fi.istrange.traveler.db.Tables;
import org.jooq.DSLContext;
import org.jooq.Table;

import java.util.Objects;

/**
 * This class host convenient method for travel card as a whole
 */
public class CardDao {
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
        if (isPersonalTravelCard(cardId)) {
            return db.fetchExists(
                    Tables.PERSONAL_CARD,
                    Tables.PERSONAL_CARD.ID.equal(cardId)
                            .and(Tables.PERSONAL_CARD.ACTIVE.equal(true))
            );
        } else if (isGroupTravelCard(cardId)) {
            return db.fetchExists(
                    Tables.GROUP_CARD,
                    Tables.GROUP_CARD.ID.equal(cardId)
                            .and(Tables.GROUP_CARD.ACTIVE.equal(true))
            );
        } else {
            throw new RuntimeException("should not be here");
        }
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
        if (isPersonalTravelCard(cardId)) {
            return db.fetchExists(
                    Tables.PERSONAL_CARD,
                    Tables.PERSONAL_CARD.ID.equal(cardId)
                            .and(Tables.PERSONAL_CARD.USERNAME_FK.equal(userName))
            );
        } else if (isGroupTravelCard(cardId)) {
            final boolean userAsCreator = db.fetchExists(
                    Tables.GROUP_CARD,
                    Tables.GROUP_CARD.ID.equal(cardId)
                            .and(Tables.GROUP_CARD.OWNER_FK.equal(userName))
            );
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
}
