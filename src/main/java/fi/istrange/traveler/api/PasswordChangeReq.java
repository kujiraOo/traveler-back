package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by aleksandr on 22.4.2017.
 */
public class PasswordChangeReq {
    private String oldPassword;
    private String newPassword;

    @JsonCreator
    public PasswordChangeReq(
            @JsonProperty("oldPassword") String oldPassword,
            @JsonProperty("newPassword") String newPassword
    ) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() { return oldPassword; }

    public String getNewPassword() { return newPassword; }
}
