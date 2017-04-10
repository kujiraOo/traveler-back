package fi.istrange.traveler.core;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Created by rohan on 4/9/17.
 */
public class PersonalTravelCard extends TravelCard{
    private User traverler;

    public PersonalTravelCard() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PersonalTravelCard that = (PersonalTravelCard) o;
        return Objects.equal(traverler, that.traverler);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), traverler);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("traverler", traverler)
                .add("super", super.toString())
                .toString();
    }

    public User getTraverler() {
        return traverler;
    }

    public void setTraverler(User traverler) {
        this.traverler = traverler;
    }
}
