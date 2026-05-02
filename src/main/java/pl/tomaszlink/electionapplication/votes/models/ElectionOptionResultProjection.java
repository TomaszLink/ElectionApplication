package pl.tomaszlink.electionapplication.votes.models;

import java.math.BigDecimal;
import java.util.UUID;

public interface ElectionOptionResultProjection {
    UUID getOptionId();
    String getOptionName();
    Long getVotesCount();
    BigDecimal getPercentage();
}
