package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Created by aleksandr on 11.4.2017.
 */
public class CardRes {
    private Long id;
    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private BigDecimal lon;
    private BigDecimal lat;
    private UserProfileRes owner;
    private Boolean active;
    private List<Long> photos;

    public CardRes(
        Long id,
        String title,
        String description,
        Date startTime,
        Date endTime,
        BigDecimal lon,
        BigDecimal lat,
        UserProfileRes owner,
        Boolean active,
        List<Long> photos
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lon = lon;
        this.lat = lat;
        this.owner = owner;
        this.active = active;
        this.photos = photos;
    }

    @JsonProperty
    public Long getID() {
        return id;
    }

    @JsonProperty
    public String getTitle() { return this.title; }

    @JsonProperty
    public String getDescription() { return this.description; }

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

    @JsonProperty
    public Boolean getActive() {
        return active;
    }

    @JsonProperty
    public List<Long> getPhotos() { return photos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardRes cardRes = (CardRes) o;
        return Objects.equal(id, cardRes.id) &&
                Objects.equal(startTime, cardRes.startTime) &&
                Objects.equal(endTime, cardRes.endTime) &&
                Objects.equal(lon, cardRes.lon) &&
                Objects.equal(lat, cardRes.lat) &&
                Objects.equal(owner, cardRes.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, startTime, endTime, lon, lat, owner);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .add("lon", lon)
                .add("lat", lat)
                .add("owner", owner)
                .toString();
    }
}
