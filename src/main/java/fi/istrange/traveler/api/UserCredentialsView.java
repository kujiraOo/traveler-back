package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by arsenii on 11/04/2017.
 */
public class UserCredentialsView {

    @NotNull
    private String name;

    @NotNull
    private String password;

    @JsonProperty(required = true)
    public String getName() {
        return name;
    }

    @JsonProperty(required = true)
    public String getPassword() {
        return password;
    }
}
