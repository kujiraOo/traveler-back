package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
/**
 * Created by arsenii on 4/8/17.
 */
public class GroupCardCreationReq extends CardCreationReq {

    private List<String> participants;

    @JsonCreator
    public GroupCardCreationReq(
            // @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("startTime") java.sql.Date startTime,
            @JsonProperty("endTime") java.sql.Date endTime,
            @JsonProperty("lon") BigDecimal lon,
            @JsonProperty("lat") BigDecimal lat,
            @JsonProperty("participants") List<String> participants
    ) {
        super(title, description, startTime, endTime, lon, lat);
        this.participants = participants;
    }

    @JsonProperty
    public List<String> getParticipants() {
        return participants;
    }

}
