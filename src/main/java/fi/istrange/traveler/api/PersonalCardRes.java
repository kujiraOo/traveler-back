package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by arsenii on 4/8/17.
 */


public class PersonalCardRes {

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
