package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fi.istrange.traveler.db.tables.pojos.TravelerUser;

import java.util.Date;
import java.util.List;

/**
 * Created by arsenii on 4/9/17.
 */
public class UserProfileRes {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final String email;
    private final Date birthday;
    private final String phone;
    private final String address;
    private final String city;
    private final String country;
    private final List<Long> photos;

    public UserProfileRes(
            String username,
            String firstName,
            String lastName,
            String email,
            String gender,
            Date birthday,
            String phone,
            String address,
            String city,
            String country,
            List<Long> photos
    ) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
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
                traveler.getFirstName(),
                traveler.getLastName(),
                traveler.getEmail(),
                traveler.getGender(),
                traveler.getBirth(),
                traveler.getPhone(),
                traveler.getAddress(),
                traveler.getCity(),
                traveler.getCountry(),
                photos
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileRes that = (UserProfileRes) o;
        return Objects.equal(username, that.username) &&
                Objects.equal(firstName, that.firstName) &&
                Objects.equal(lastName, that.lastName) &&
                Objects.equal(gender, that.gender) &&
                Objects.equal(email, that.email) &&
                Objects.equal(birthday, that.birthday) &&
                Objects.equal(phone, that.phone) &&
                Objects.equal(address, that.address) &&
                Objects.equal(city, that.city) &&
                Objects.equal(country, that.country) &&
                Objects.equal(photos, that.photos);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username, firstName, lastName, gender, email, birthday, phone, address, city, country, photos);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("gender", gender)
                .add("email", email)
                .add("birthday", birthday)
                .add("phone", phone)
                .add("address", address)
                .add("city", city)
                .add("country", country)
                .add("photos", photos)
                .toString();
    }
}
