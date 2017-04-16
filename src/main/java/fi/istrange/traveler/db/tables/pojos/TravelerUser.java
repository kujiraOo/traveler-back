/*
 * This file is generated by jOOQ.
*/
package fi.istrange.traveler.db.tables.pojos;


import java.io.Serializable;
import java.sql.Date;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TravelerUser implements Serializable {

    private static final long serialVersionUID = -2010630712;

    private String username;
    private Date   birth;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private Long   photo;
    private String firstName;
    private String lastName;

    public TravelerUser() {}

    public TravelerUser(TravelerUser value) {
        this.username = value.username;
        this.birth = value.birth;
        this.gender = value.gender;
        this.email = value.email;
        this.phone = value.phone;
        this.address = value.address;
        this.city = value.city;
        this.country = value.country;
        this.photo = value.photo;
        this.firstName = value.firstName;
        this.lastName = value.lastName;
    }

    public TravelerUser(
        String username,
        Date   birth,
        String gender,
        String email,
        String phone,
        String address,
        String city,
        String country,
        Long   photo,
        String firstName,
        String lastName
    ) {
        this.username = username;
        this.birth = birth;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
        this.photo = photo;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getPhoto() {
        return this.photo;
    }

    public void setPhoto(Long photo) {
        this.photo = photo;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TravelerUser (");

        sb.append(username);
        sb.append(", ").append(birth);
        sb.append(", ").append(gender);
        sb.append(", ").append(email);
        sb.append(", ").append(phone);
        sb.append(", ").append(address);
        sb.append(", ").append(city);
        sb.append(", ").append(country);
        sb.append(", ").append(photo);
        sb.append(", ").append(firstName);
        sb.append(", ").append(lastName);

        sb.append(")");
        return sb.toString();
    }
}