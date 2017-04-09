package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public UserProfileUpdateReq(
            @JsonProperty("password") String password,
            @JsonProperty("email") String email,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName
    ) {
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }
}
