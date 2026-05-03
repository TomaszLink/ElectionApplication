package pl.tomaszlink.electionapplication.votes.commands;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VoteCommand(
        @NotNull UUID electionId,
        @NotNull UUID voterId,
        @NotNull UUID optionId
) {
}
