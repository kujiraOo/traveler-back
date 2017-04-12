package fi.istrange.traveler.api;

import java.sql.Date;

/**
 * Created by arsenii on 4/8/17.
 */
public class GroupCardRes extends CardRes {

    private Integer id;
    private Date startTime;
    private Date endTime;
    private Long lon;
    private Long lat;

    public GroupCardRes(
        Integer id,
        Date startTime,
        Date endTime,
        Long lon,
        Long lat
    ) {
        super(id, startTime, endTime, lon, lat);
    }
}
