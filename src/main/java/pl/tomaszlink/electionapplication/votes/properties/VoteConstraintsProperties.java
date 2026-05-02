package pl.tomaszlink.electionapplication.votes.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vote.constraints")
public record VoteConstraintsProperties(String votePerElectionUnique) {
}
