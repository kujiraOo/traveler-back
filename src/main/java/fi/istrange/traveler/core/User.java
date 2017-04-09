package fi.istrange.traveler.core;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import fi.istrange.traveler.api.UserRegistrationReq;

/**
 * Created by rohan on 4/9/17.
 */
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    public User(){};

    @Override
    public int hashCode() {
        return Objects.hashCode(id, username, password);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final User other = (User) obj;
        return Objects.equal(this.id, other.id)
                && Objects.equal(this.username, other.username)
                && Objects.equal(this.password, other.password);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("password", password)
                .add("email", email)
                .add("firstName", firstName)
                .add("lastName", lastName).toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
