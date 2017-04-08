package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by arsenii on 4/8/17.
 */


public class PersonalCardRes {

    //TODO just placeholder, populate later or replace with smth better

    private long id = 25;

    public PersonalCardRes() {}

    @JsonProperty
    public long getId() {
        return id;
    }
}
