package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by arsenii on 4/8/17.
 */
public class GroupCardUpdateReq {

    //TODO just placeholder, populate later or replace with smth better

    private Date arrivalDateTime;

    public GroupCardUpdateReq() {}

    @JsonProperty
    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }
}
