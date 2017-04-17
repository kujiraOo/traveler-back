package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.pojos.PersonalCard;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by aleksandr on 16.4.2017.
 */
public class PersonalCardCustomDao {

    public List<PersonalCard> fetchByPosition(
            BigDecimal lat,
            BigDecimal lon,
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        // TODO: change 1 to the value that atually makes sense
        return fetch(
                Tables.PERSONAL_CARD.LAT.between(
                        lat.subtract(new BigDecimal(1)),
                        lat.add(new BigDecimal(1))
                )
                .and(Tables.PERSONAL_CARD.LON.between(
                        lat.subtract(new BigDecimal(1)),
                        lat.add(new BigDecimal(1)))
                ),
                fetchArchived,
                offset,
                database
        );
    }

    public List<PersonalCard> fetchByUsername(
            String username,
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        return fetch(
                Tables.PERSONAL_CARD.USERNAME_FK.equal(username),
                fetchArchived,
                offset,
                database
        );
    }

    public List<PersonalCard> fetchAll(
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        return fetch(
                Tables.PERSONAL_CARD.ID.greaterOrEqual(0L), // workaround because not able to pass boolean
                fetchArchived,
                offset,
                database
        );
    }


    public List<PersonalCard> fetch(
            Condition condition,
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        return database.select()
                .from(Tables.PERSONAL_CARD)
                .where(
                        condition
                        .and(Tables.PERSONAL_CARD.ACTIVE.equal(true)
                                .or(Tables.PERSONAL_CARD.ACTIVE.equal(!fetchArchived)))
                )
                .offset(offset)
                .limit(20)
                .fetch()
                .map(p -> new PersonalCard(
                        p.getValue(Tables.PERSONAL_CARD.ID),
                        p.getValue(Tables.PERSONAL_CARD.START_TIME),
                        p.getValue(Tables.PERSONAL_CARD.END_TIME),
                        p.getValue(Tables.PERSONAL_CARD.LON),
                        p.getValue(Tables.PERSONAL_CARD.LAT),
                        p.getValue(Tables.PERSONAL_CARD.USERNAME_FK),
                        p.getValue(Tables.PERSONAL_CARD.ACTIVE)
                ));
    }
}
