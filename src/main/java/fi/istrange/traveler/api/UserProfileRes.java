package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;

import java.util.Date;
import java.util.List;

/**
 * Created by arsenii on 4/9/17.
 */
public class UserProfileRes {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthday;
    private String phone;
    private String address;
    private String city;
    private String country;
    private List<Long> photos;

    @JsonCreator
    public UserProfileRes(
            @JsonProperty("password") String username,
            @JsonProperty("email") String email,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            Date birthday,
            String phone,
            String address,
            String city,
            String country,
            List<Long> photos
    ) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
        this.photos = photos;
    }

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
    public String getUsername() {
        return username;
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
    public List<Long> getPhotos() {
        return photos;
    }

    public static UserProfileRes fromEntity(TravelerUser traveler, List<Long> photos) {
        return new UserProfileRes(
                traveler.getUsername(),
                traveler.getEmail(),
                traveler.getFirstName(),
                traveler.getLastName(),
                traveler.getBirth(),
                traveler.getPhone(),
                traveler.getAddress(),
                traveler.getCity(),
                traveler.getCountry(),
                photos
        );
    }
}
