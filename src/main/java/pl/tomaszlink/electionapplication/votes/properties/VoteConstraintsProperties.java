package pl.tomaszlink.electionapplication.votes.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vote.constraints")
public record VoteConstraintsProperties(@NotNull String votePerElectionUnique) {
}
