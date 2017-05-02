package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.tables.records.MatchRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

import static fi.istrange.traveler.db.Tables.MATCH;

/**
 * Created by rohan on 4/23/17.
 */
public class MatchCustomDao {

    public static void createOrUpdateLike(Long likerCardId, Long likedCardId, boolean like, DSLContext db) {
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

        db.select().from(MATCH).stream().forEach(p -> {
            System.out.println("New!!!");
            System.out.println(p.get(MATCH.LIKER_CARD_ID).toString());
            System.out.println(p.get(MATCH.LIKED_CARD_ID).toString());
            System.out.println(p.get(MATCH.LIKE_DECISION).toString());
        });
    }

    /**
     * @return true if card1Id, card2Id like each other before
     */
    public static boolean isMatch(Long card1Id, Long card2Id, DSLContext db) {
        return db.fetchExists(
                MATCH,
                likeCondition(card1Id, card2Id, true)
        ) && db.fetchExists(
                MATCH,
                likeCondition(card2Id, card1Id, true)
        );
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
