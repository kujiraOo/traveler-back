package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

/**
 * Created by arsenii on 4/9/17.
 */
public class UserProfileUpdateReq {

    // TODO a placeholder to populate with appropriate values later

    private String firstName;
    private String lastName;
    private String email;
    private Date birthday;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String gender;

    @JsonCreator
    public UserProfileUpdateReq(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("email") String email,
            @JsonProperty("gender") String gender,
            @JsonProperty("birthday") Date birthday,
            @JsonProperty("phone") String phone,
            @JsonProperty("address") String address,
            @JsonProperty("city") String city,
            @JsonProperty("country") String country
    ) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
    }
    @JsonProperty
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty
    public String getLastName() {
        return lastName;
    }

    @JsonProperty()
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public Date getBirthday() {
        return birthday;
    }

    @JsonProperty
    public String getPhone() {
        return phone;
    }

    @JsonProperty
    public String getAddress() {
        return address;
    }

    @JsonProperty
    public String getCity() {
        return city;
    }

    @JsonProperty
    public String getCountry() {
        return country;
    }

    @JsonProperty
    public String getGender() {
        return gender;
    }
/*
    public static UserProfileUpdateReq fromEntity (TravelerUser traveler) {
        return new UserProfileRes(
                traveler.getFirstName(),
                traveler.getLastName(),
                traveler.getEmail(),
                traveler.getBirth(),
                traveler.getPhone(),
                traveler.getAddress(),
                traveler.getCity(),
                traveler.getCountry()
        );
    }
    */
}
