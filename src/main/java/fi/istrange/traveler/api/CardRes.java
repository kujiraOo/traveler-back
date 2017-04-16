package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by aleksandr on 11.4.2017.
 */
public class CardRes {
    private Long id;
    private Date startTime;
    private Date endTime;
    private BigDecimal lon;
    private BigDecimal lat;
    private UserProfileRes owner;

    public CardRes(
        Long id,
        Date startTime,
        Date endTime,
        BigDecimal lon,
        BigDecimal lat,
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
    public BigDecimal getLatitude() {
        return lat;
    }

    @JsonProperty
    public BigDecimal getLongitude() {
        return lon;
    }

    @JsonProperty
    public UserProfileRes getOwner() {
        return owner;
    }
}
