package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by arsenii on 4/8/17.
 */
public class PersonalCardCreationReq {

    //TODO just placeholder, populate later or replace with smth better

    private Date arrivalDateTime;

    public PersonalCardCreationReq() {}

    @JsonProperty
    public Date getArrivalDateTime() {
        return arrivalDateTime;
    }
}
