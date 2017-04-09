package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by arsenii on 4/8/17.
 */


public class PersonalCardRes {

    //TODO just placeholder, populate later or replace with smth better

    private long id = 25;
    private Date arrivalDateTime;

    public PersonalCardRes() {}

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }
}
