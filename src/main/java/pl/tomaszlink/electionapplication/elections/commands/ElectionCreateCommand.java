package pl.tomaszlink.electionapplication.elections.commands;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

public record ElectionCreateCommand(
        @NotNull String name,
        String description,
        @NotNull OffsetDateTime startDate,
        @NotNull OffsetDateTime endDate,
        @NotNull List<ElectionOptionCreateCommand> options
) {
}
