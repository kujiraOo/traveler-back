package fi.istrange.traveler.api;

/**
 * Created by arsenii on 4/8/17.
 */
public class PersonalCardUpdateReq extends CardUpdateReq {

    public PersonalCardUpdateReq(
            java.sql.Date startTime,
            java.sql.Date endTime,
            Long lon,
            Long lat
    ) {
        super(startTime, endTime, lon, lat);
    }
}
