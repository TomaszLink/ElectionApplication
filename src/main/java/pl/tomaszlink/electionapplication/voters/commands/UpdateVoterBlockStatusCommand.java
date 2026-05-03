package pl.tomaszlink.electionapplication.voters.commands;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateVoterBlockStatusCommand(
        @NotNull UUID id,
        boolean blocked) {
}
