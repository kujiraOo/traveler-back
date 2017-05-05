package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Date;
/**
 * Created by aleksandr on 11.4.2017.
 */
public class CardCreationReq {

    // private Long id;
    private Date startTime;
    private Date endTime;
    private BigDecimal lon;
    private BigDecimal lat;
    private String title;
    private String description;

    @JsonCreator
    public CardCreationReq(
            // @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("startTime") Date startTime,
            @JsonProperty("endTime") Date endTime,
            @JsonProperty("lon") BigDecimal lon,
            @JsonProperty("lat") BigDecimal lat
    ) {
        // this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lon = lon;
        this.lat = lat;
    }
/*
    public Long getId() {
        return id;
    }
*/
    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public BigDecimal getLat() {
        return lat;
    }
}
