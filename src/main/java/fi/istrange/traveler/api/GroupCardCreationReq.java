package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
/**
 * Created by arsenii on 4/8/17.
 */
public class GroupCardCreationReq extends CardCreationReq {

    private List<String> participants;

    public GroupCardCreationReq(
            Long id,
            java.sql.Date startTime,
            java.sql.Date endTime,
            Long lon,
            Long lat,
            List<String> participants
    ) {
        super(id, startTime, endTime, lon, lat);
        this.participants = participants;
    }

    @JsonProperty
    public List<String> getParticipants() {
        return participants;
    }

}
