package fi.istrange.traveler.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by rohan on 4/22/17.
 */
public class MatchForCardRes {
    private final List<PersonalCardRes> matchedPersonalCard;
    private final List<GroupCardRes> matchedGroupCard;

    public MatchForCardRes(
            List<PersonalCardRes> matchedPersonalCard,
            List<GroupCardRes> matchedGroupCard
    ) {
        this.matchedPersonalCard = matchedPersonalCard;
        this.matchedGroupCard = matchedGroupCard;
    }

    @JsonProperty("matchedPersonalCard")
    public List<PersonalCardRes> getMatchedPersonalCard() {
        return matchedPersonalCard;
    }

    @JsonProperty("matchedGroupCard")
    public List<GroupCardRes> getMatchedGroupCard() {
        return matchedGroupCard;
    }
}
