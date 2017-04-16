package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fi.istrange.traveler.db.tables.pojos.PersonalCard;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;

import java.math.BigDecimal;

/**
 * Created by arsenii on 4/8/17.
 */


public class PersonalCardRes extends CardRes {
    @JsonCreator
    public PersonalCardRes(
        @JsonProperty("id") Long id,
        @JsonProperty("startTime") java.sql.Date startTime,
        @JsonProperty("endTime") java.sql.Date endTime,
        @JsonProperty("lon") BigDecimal lon,
        @JsonProperty("lat") BigDecimal lat,
        @JsonProperty("owner") UserProfileRes owner
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
