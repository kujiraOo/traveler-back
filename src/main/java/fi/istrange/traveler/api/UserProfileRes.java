package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by arsenii on 4/9/17.
 */
public class UserProfileRes {

    // TODO a placeholder to populate with appropriate values later

    private String userName;
    private String firstName;
    private String lastName;
    private String email;

    public UserProfileRes() {}

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
}
