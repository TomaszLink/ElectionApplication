package pl.tomaszlink.electionapplication.elections.commands;

import jakarta.validation.constraints.NotNull;

public record ElectionOptionCreateCommand(
        @NotNull String name,
        String description) {
}
