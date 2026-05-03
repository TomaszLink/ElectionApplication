package pl.tomaszlink.electionapplication.results.models;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ElectionOptionResult(
        @NotNull UUID optionId,
        @NotNull String optionName,
        @NotNull Long votesCount,
        @NotNull Double percentage
) {
}
