package pl.tomaszlink.electionapplication.elections.commands;


import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ElectionUpdateCommand(
        @NotNull UUID id,
        @NotNull String name,
        String description,
        @NotNull OffsetDateTime startDate,
        @NotNull OffsetDateTime endDate,
        @NotNull List<ElectionOptionUpdateCommand> options) {
}
