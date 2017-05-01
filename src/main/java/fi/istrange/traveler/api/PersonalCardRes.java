package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fi.istrange.traveler.db.tables.pojos.Card;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by arsenii on 4/8/17.
 */


public class PersonalCardRes extends CardRes {
    @JsonCreator
    public PersonalCardRes(
        @JsonProperty("id") Long id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("startTime") java.sql.Date startTime,
        @JsonProperty("endTime") java.sql.Date endTime,
        @JsonProperty("lon") BigDecimal lon,
        @JsonProperty("lat") BigDecimal lat,
        @JsonProperty("owner") UserProfileRes owner,
        @JsonProperty("photos") List<Long> photos
    ) {
        super(id, title, description, startTime, endTime, lon, lat, owner, photos);
    }

    public static PersonalCardRes fromEntity(
            Card card,
            TravelerUser user,
            List<Long> userPhotos,
            List<Long> cardPhotos
    ) {
        return new PersonalCardRes(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getStartTime(),
                card.getEndTime(),
                card.getLon(),
                card.getLat(),
                UserProfileRes.fromEntity(user, userPhotos),
                cardPhotos
        );
    }
}
