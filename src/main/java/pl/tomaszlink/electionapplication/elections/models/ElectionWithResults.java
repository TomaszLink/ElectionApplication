package pl.tomaszlink.electionapplication.elections.models;

import jakarta.validation.constraints.NotNull;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.results.models.ElectionResultsSummary;

public record ElectionWithResults(
        @NotNull ElectionEntity electionEntity,
        ElectionResultsSummary resultsSummary
        ) {
}
