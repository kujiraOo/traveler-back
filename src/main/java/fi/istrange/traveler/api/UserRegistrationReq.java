package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

/**
 * Created by arsenii on 4/9/17.
 */
public class UserRegistrationReq {
    private String username;
    private Date birth;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String firstName;
    private String lastName;
    private String password;

    @JsonCreator
    public UserRegistrationReq(
            @JsonProperty("username")String username,
            @JsonProperty("password")String password,
            @JsonProperty("birth")Date birth,
            @JsonProperty("gender") String gender,
            @JsonProperty("email")String email,
            @JsonProperty("phone")String phone,
            @JsonProperty("address")String address,
            @JsonProperty("city")String city,
            @JsonProperty("country") String country,
            @JsonProperty("firstName")String firstName,
            @JsonProperty("lastName")String lastName
    ) {
        this.username = username;
        this.password = password;
        this.birth = birth;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirth() {
        return this.birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
