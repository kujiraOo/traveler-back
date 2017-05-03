package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import fi.istrange.traveler.db.tables.pojos.Card;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by arsenii on 4/8/17.
 */
public class GroupCardRes extends CardRes {

    private List<String> participants;

    public GroupCardRes(
        Long id,
        String title,
        String description,
        Date startTime,
        Date endTime,
        BigDecimal lon,
        BigDecimal lat,
        UserProfileRes owner,
        List<String> participants,
        List<Long> photos
    ) {
        super(id, title, description, startTime, endTime, lon, lat, owner, photos);
        this.participants = participants;
    }

    @JsonProperty
    public List<String> getParticipants() {
        return participants;
    }

    public static GroupCardRes fromEntity(
            Card card,
            Set<TravelerUser> participants,
            TravelerUser owner,
            List<Long> userPhotos,
            List<Long> cardPhotos
    ) {
        return new GroupCardRes(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getStartTime(),
                card.getEndTime(),
                card.getLon(),
                card.getLat(),
                UserProfileRes.fromEntity(owner, userPhotos),
                participants.stream()
                        .map(p -> p.getUsername())
                        .collect(Collectors.toList()),
                cardPhotos
        );
    }
}
