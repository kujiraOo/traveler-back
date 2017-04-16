package fi.istrange.traveler.dao;

import fi.istrange.traveler.db.Tables;
import fi.istrange.traveler.db.tables.pojos.PersonalCard;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by aleksandr on 16.4.2017.
 */
public class PersonalCardLocationDao {

    public List<PersonalCard> getCardsByLocation(
            BigDecimal lat,
            BigDecimal lon,
            DSLContext database
    ) {
        // TODO: change 1 to the value that atually makes sense
        return database.select()
                .from(Tables.PERSONAL_CARD)
                .where(Tables.PERSONAL_CARD.LAT.between(lat.subtract(new BigDecimal(1)), lat.add(new BigDecimal(1))))
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
