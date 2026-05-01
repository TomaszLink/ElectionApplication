package pl.tomaszlink.electionapplication.voters.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "constraints")
public record ConstraintsProperties(String peselUnique) {
}
