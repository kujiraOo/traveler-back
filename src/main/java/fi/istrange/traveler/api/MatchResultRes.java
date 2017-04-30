package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by rohan on 4/22/17.
 */
public class MatchResultRes {
    @NotNull
    public final Boolean matched;

    public MatchResultRes(Boolean matched) {
        this.matched = matched;
    }

    @JsonProperty("matched")
    public Boolean isAMatch() {
        return matched;
    }
}
