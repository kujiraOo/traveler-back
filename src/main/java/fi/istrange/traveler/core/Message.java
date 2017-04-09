package fi.istrange.traveler.core;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Date;

/**
 * Created by rohan on 4/9/17.
 */
public class Message {
    private Long id;
    private TravelCard sender;
    private Date sentDate;

    public Message(){}

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("sender", sender)
                .add("sentDate", sentDate)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equal(id, message.id) &&
                Objects.equal(sender, message.sender) &&
                Objects.equal(sentDate, message.sentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, sender, sentDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TravelCard getSender() {
        return sender;
    }

    public void setSender(TravelCard sender) {
        this.sender = sender;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}
