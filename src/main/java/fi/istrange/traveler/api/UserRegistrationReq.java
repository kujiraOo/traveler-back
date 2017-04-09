package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by arsenii on 4/9/17.
 */
public class UserRegistrationReq {

    // TODO a placeholder to populate with appropriate values later

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    public UserRegistrationReq() {}

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty
    public String getLastName() {
        return lastName;
    }
}
