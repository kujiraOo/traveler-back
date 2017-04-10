package fi.istrange.traveler.core;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Date;
import java.util.List;

/**
 * Created by rohan on 4/9/17.
 */
public class Conversation {
    private List<TravelCard> participants;
    private List<Message> messages;
    private Date startTime;

    public Conversation(){}

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("participants", participants)
                .add("messages", messages)
                .add("startTime", startTime)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return Objects.equal(participants, that.participants) &&
                Objects.equal(messages, that.messages) &&
                Objects.equal(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(participants, messages, startTime);
    }

    public List<TravelCard> getParticipants() {
        return participants;
    }

    public void setParticipants(List<TravelCard> participants) {
        this.participants = participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
