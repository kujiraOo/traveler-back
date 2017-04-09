package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by arsenii on 4/8/17.
 */
public class PersonalCardUpdateReq {

    //TODO just placeholder, populate later or replace with smth better
    private Date arrivalDateTime;

    public PersonalCardUpdateReq() {}

    @JsonProperty
    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }
}
