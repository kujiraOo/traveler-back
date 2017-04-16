package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.util.List;
/**
 * Created by arsenii on 4/8/17.
 */
public class GroupCardUpdateReq extends CardUpdateReq {

    private List<String> participants;

    public GroupCardUpdateReq(
            Date startTime,
            Date endTime,
            Long lon,
            Long lat,
            List<String> participants
    ) {
        super(startTime, endTime, lon, lat);
        this.participants = participants;
    }

    @JsonProperty
    public List<String> getParticipants() {
        return participants;
    }
}
