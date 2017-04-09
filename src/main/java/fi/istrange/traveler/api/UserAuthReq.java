package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by arsenii on 4/9/17.
 */
public class UserAuthReq {

    // TODO just a placeholder, probably has to be replaced with domain Credentials class
    private String username;
    private String password;

    public UserAuthReq() {}

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }
}
