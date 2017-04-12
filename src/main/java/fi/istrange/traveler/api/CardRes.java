package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import fi.istrange.traveler.db.tables.pojos.*;

import java.sql.Date;

/**
 * Created by aleksandr on 11.4.2017.
 */
public class CardRes {
    private Integer id;
    private Date startTime;
    private Date endTime;
    private Long lon;
    private Long lat;

    public CardRes(
        Integer id,
        Date startTime,
        Date endTime,
        Long lon,
        Long lat
    ) {

    }

    @JsonProperty
    public Integer getID() {
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

    public static CardRes fromEntity(GroupCard card) {
        return new CardRes(
                card.getId(),
                card.getStartTime(),
                card.getEndTime(),
                card.getLon(),
                card.getLat()
        );
    }

    public static CardRes fromEntity(PersonalCard card) {
        return new CardRes(
                card.getId(),
                card.getStartTime(),
                card.getEndTime(),
                card.getLon(),
                card.getLat()
        );
    }
}
