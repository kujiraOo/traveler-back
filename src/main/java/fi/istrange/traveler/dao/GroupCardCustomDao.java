package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.pojos.GroupCard;
import org.jooq.DSLContext;
import org.jooq.Condition;

import java.math.BigDecimal;
import java.util.List;
/**
 * Created by aleksandr on 16.4.2017.
 */
public class GroupCardCustomDao {

    public List<GroupCard> fetchByPosition(
            BigDecimal lat,
            BigDecimal lon,
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        // TODO: change 1 to the value that atually makes sense
        return fetch(
                Tables.GROUP_CARD.LAT.between(
                        lat.subtract(new BigDecimal(1)),
                        lat.add(new BigDecimal(1))
                )
                .and(Tables.GROUP_CARD.LON.between(
                        lat.subtract(new BigDecimal(1)),
                        lat.add(new BigDecimal(1)))
                ),
                fetchArchived,
                offset,
                database
        );
    }

    public List<GroupCard> fetchByUsername(
            String username,
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        return fetch(
                Tables.GROUP_CARD.OWNER_FK.equal(username),
                fetchArchived,
                offset,
                database
        );
    }

    public List<GroupCard> fetchAll(
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        return fetch(
                Tables.GROUP_CARD.ID.greaterOrEqual(0L), // workaround because notable to pass boolean
                fetchArchived,
                offset,
                database
        );
    }


    public List<GroupCard> fetch(
            Condition condition,
            boolean fetchArchived,
            int offset,
            DSLContext database
    ) {
        return database.select()
                .from(Tables.GROUP_CARD)
                .where(
                        condition
                        .and(Tables.GROUP_CARD.ACTIVE.equal(true)
                                .or(Tables.GROUP_CARD.ACTIVE.equal(!fetchArchived)))
                )
                .offset(offset)
                .limit(20)
                .fetch()
                .map(p -> new GroupCard(
                        p.getValue(Tables.GROUP_CARD.ID),
                        p.getValue(Tables.GROUP_CARD.START_TIME),
                        p.getValue(Tables.GROUP_CARD.END_TIME),
                        p.getValue(Tables.GROUP_CARD.LON),
                        p.getValue(Tables.GROUP_CARD.LAT),
                        p.getValue(Tables.GROUP_CARD.OWNER_FK),
                        p.getValue(Tables.GROUP_CARD.ACTIVE)
                ));
    }
}
