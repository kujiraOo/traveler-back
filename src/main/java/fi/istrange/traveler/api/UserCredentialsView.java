package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by arsenii on 11/04/2017.
 */
public class UserCredentialsView {
    private String name;
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
