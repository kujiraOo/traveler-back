package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.records.MatchRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import static fi.istrange.traveler.db.Tables.MATCH;

import java.util.List;
import java.util.Optional;

/**
 * Created by rohan on 4/23/17.
 */
public class MatchCustomDao {

    public static void createOrUpdateMatch(Long likerCardId, Long likedCardId, boolean like, DSLContext db) {
        Optional<MatchRecord> matchRecord = db.
                selectFrom(MATCH)
                .where(
                        MATCH.LIKER_CARD_ID.equal(likerCardId)
                                .and(MATCH.LIKED_CARD_ID.equal(likedCardId))
                ).fetchOptional();

        final MatchRecord match;
        if (matchRecord.isPresent()) {
            match = matchRecord.get();
        } else {
            match = db.newRecord(MATCH);
            match.setLikerCardId(likerCardId);
            match.setLikedCardId(likedCardId);
        }

        match.setLikeDecision(like);
        match.store();
    }

    /**
     * @return true iff card1Id, card2Id like each other before
     */
    public static boolean isMatch(Long card1Id, Long card2Id, DSLContext db) {
        int count = db.fetchCount(
                MATCH,
                likeCondition(card1Id, card2Id, true)
                        .and(likeCondition(card2Id, card1Id, true))
        );
        return count == 2;
    }

    private static Condition likeCondition(Long liker, Long liked, boolean like) {
        return MATCH.LIKER_CARD_ID.equal(liker)
                .and(MATCH.LIKED_CARD_ID.equal(liked))
                .and(MATCH.LIKE_DECISION.equal(like));
    }

    public static List<Long> getMatchingFor(Long likerCardId, DSLContext db) {
        // list of card that likerCard likes
        List<Long> liked = db.select()
                .from(MATCH)
                .where(MATCH.LIKER_CARD_ID.equal(likerCardId)
                        .and(MATCH.LIKE_DECISION.equal(true)))
                .fetch(MATCH.LIKED_CARD_ID);
        // list of cards that like the liker back
        List<Long> match = db.select()
                .from(MATCH)
                .where(MATCH.LIKER_CARD_ID.in(liked)
                        .and(MATCH.LIKED_CARD_ID.equal(likerCardId))
                        .and(MATCH.LIKE_DECISION.equal(true)))
                .fetch(MATCH.LIKER_CARD_ID);
        return match;
    }

}
