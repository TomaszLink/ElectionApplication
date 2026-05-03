package pl.tomaszlink.electionapplication.voters.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "voter.constraints")
public record VoterConstraintsProperties(@NotNull String peselUnique) {
}
