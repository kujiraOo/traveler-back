package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by arsenii on 4/8/17.
 */
public class PersonalCardUpdateReq extends CardUpdateReq {
    @JsonCreator
    public PersonalCardUpdateReq(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("startTime") java.sql.Date startTime,
            @JsonProperty("endTime") java.sql.Date endTime,
            @JsonProperty("lon") BigDecimal lon,
            @JsonProperty("lat") BigDecimal lat
            ) {
        super(title, description, startTime, endTime, lon, lat);
    }
}
