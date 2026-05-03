package pl.tomaszlink.electionapplication.results.models;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ElectionResultsSummary(
        long votesTotalCount,
        @NotNull List<ElectionOptionResult> optionResults
) {
}
