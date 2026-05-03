package pl.tomaszlink.electionapplication.elections.commands;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ElectionOptionUpdateCommand(
        UUID id,
        @NotNull String name,
        String description) {
}
