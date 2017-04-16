package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

/**
 * Created by aleksandr on 11.4.2017.
 */
public class CardRes {
    private Long id;
    private Date startTime;
    private Date endTime;
    private Long lon;
    private Long lat;
    private UserProfileRes owner;

    public CardRes(
        Long id,
        Date startTime,
        Date endTime,
        Long lon,
        Long lat,
        UserProfileRes owner
    ) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lon = lon;
        this.lat = lat;
        this.owner = owner;
    }

    @JsonProperty
    public Long getID() {
        return id;
    }

    @JsonProperty
    public Date getStartTime() {
        return startTime;
    }

    @JsonProperty
    public Date getEndTime() {
        return endTime;
    }

    @JsonProperty
    public Long getLatitude() {
        return lat;
    }

    @JsonProperty
    public Long getLongitude() {
        return lon;
    }

    @JsonProperty
    public UserProfileRes getOwner() {
        return owner;
    }
}
