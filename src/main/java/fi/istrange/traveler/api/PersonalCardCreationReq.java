package fi.istrange.traveler.api;

/**
 * Created by arsenii on 4/8/17.
 */
public class PersonalCardCreationReq extends CardCreationReq {

    public PersonalCardCreationReq(
            Long id,
            java.sql.Date startTime,
            java.sql.Date endTime,
            Long lon,
            Long lat
    ) {
        super(id, startTime, endTime, lon, lat);
    }

}
