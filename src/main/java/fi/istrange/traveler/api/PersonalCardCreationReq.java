package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by arsenii on 4/8/17.
 */
public class PersonalCardCreationReq {

    //TODO just placeholder, populate later or replace with smth better

    private long userId;

    public PersonalCardCreationReq() {}

    @JsonProperty
    public long getUserId() {
        return userId;
    }
}
