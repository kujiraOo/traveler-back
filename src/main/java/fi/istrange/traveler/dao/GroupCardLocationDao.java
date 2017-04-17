package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.pojos.GroupCard;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.util.List;
/**
 * Created by aleksandr on 16.4.2017.
 */
public class GroupCardLocationDao {

    public List<GroupCard> getCardsByLocation(
            BigDecimal lat,
            BigDecimal lon,
            DSLContext database
    ) {
        // TODO: change 1 to the value that atually makes sense
        return database.select()
                .from(Tables.GROUP_CARD)
                .where(
                    Tables.GROUP_CARD.LAT.between(
                        lat.subtract(new BigDecimal(1)),
                        lat.add(new BigDecimal(1))
                    )
                    .and(Tables.GROUP_CARD.LON.between(
                        lat.subtract(new BigDecimal(1)),
                        lat.add(new BigDecimal(1)))
                    )
                )
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
