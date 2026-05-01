package pl.tomaszlink.electionapplication.elections.models;

import java.util.UUID;

public record ElectionOptionUpdateCommand(UUID id, String name, String description) {
}
