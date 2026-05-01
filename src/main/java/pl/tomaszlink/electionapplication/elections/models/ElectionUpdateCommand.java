package pl.tomaszlink.electionapplication.elections.models;

import java.time.OffsetDateTime;

public record ElectionUpdateCommand(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate) {
}
