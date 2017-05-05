package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
/**
 * Created by arsenii on 4/8/17.
 */
public class GroupCardUpdateReq extends CardUpdateReq {

    private List<String> participants;

    @JsonCreator
    public GroupCardUpdateReq(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("startTime") Date startTime,
            @JsonProperty("endTime") Date endTime,
            @JsonProperty("lon") BigDecimal lon,
            @JsonProperty("lat") BigDecimal lat,
            @JsonProperty("participants") List<String> participants
    ) {
        super(title, description, startTime, endTime, lon, lat);
        this.participants = participants;
    }

    public List<String> getParticipants() {
        return participants;
    }
}
