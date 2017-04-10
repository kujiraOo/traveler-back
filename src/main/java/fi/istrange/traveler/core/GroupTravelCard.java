package fi.istrange.traveler.core;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;

/**
 * Created by rohan on 4/9/17.
 */
public class GroupTravelCard extends TravelCard{
    private List<User> travellers;

    public GroupTravelCard(){}

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("super", super.toString())
                .add("travellers", travellers)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GroupTravelCard that = (GroupTravelCard) o;
        return Objects.equal(travellers, that.travellers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), travellers);
    }

    public List<User> getTravellers() {
        return travellers;
    }

    public void setTravellers(List<User> travellers) {
        this.travellers = travellers;
    }
}
