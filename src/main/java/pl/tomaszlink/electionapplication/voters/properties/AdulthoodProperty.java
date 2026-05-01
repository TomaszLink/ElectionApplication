package pl.tomaszlink.electionapplication.voters.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "voters.adulthood")
public record AdulthoodProperty(int thresholdAge) {
}
