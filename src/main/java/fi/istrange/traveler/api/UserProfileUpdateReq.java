package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by arsenii on 4/9/17.
 */
public class UserProfileUpdateReq {

    // TODO a placeholder to populate with appropriate values later

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserProfileUpdateReq() {}

    @JsonProperty
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty
    public String getLastName() {
        return lastName;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }
}
