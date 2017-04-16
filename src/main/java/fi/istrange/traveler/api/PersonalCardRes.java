package fi.istrange.traveler.api;

import fi.istrange.traveler.db.tables.pojos.PersonalCard;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;

/**
 * Created by arsenii on 4/8/17.
 */


public class PersonalCardRes extends CardRes {

    public PersonalCardRes(
        Long id,
        java.sql.Date startTime,
        java.sql.Date endTime,
        Long lon,
        Long lat,
        UserProfileRes owner
    ) {
        super(id, startTime, endTime, lon, lat, owner);
    }

    public static PersonalCardRes fromEntity(PersonalCard card, TravelerUser user) {
        return new PersonalCardRes(
                card.getId(),
                card.getStartTime(),
                card.getEndTime(),
                card.getLon(),
                card.getLat(),
                UserProfileRes.fromEntity(user)
        );
    }
}
