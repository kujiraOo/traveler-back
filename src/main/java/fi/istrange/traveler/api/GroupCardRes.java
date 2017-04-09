package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by arsenii on 4/8/17.
 */
public class GroupCardRes {

    //TODO just placeholder, populate later or replace with smth better

    private long id;
    private Date arrivalDateTime;

    public GroupCardRes() {}

    @JsonProperty
    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }

    @JsonProperty
    public long getId() {
        return id;
    }
}
