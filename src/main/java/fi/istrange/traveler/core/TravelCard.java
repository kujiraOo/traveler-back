package fi.istrange.traveler.core;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Date;

/**
 * Created by rohan on 4/9/17.
 */
abstract class TravelCard {

    private Long id;
    private Date startTime;
    private Date endTime;
    private double lon;
    private double lah;

    public TravelCard(){}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final TravelCard other = (TravelCard) obj;
        return Objects.equal(this.id, other.id)
                && Objects.equal(this.startTime, other.startTime)
                && Objects.equal(this.endTime, other.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, startTime, endTime);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("startTime", startTime)
                .add("endTime", endTime).toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLah() {
        return lah;
    }

    public void setLah(double lah) {
        this.lah = lah;
    }
}
