package pl.tomaszlink.electionapplication.results.models;

import java.util.UUID;

public interface ElectionOptionResultProjection {
    UUID getOptionId();
    String getOptionName();
    Long getVotesCount();
}
