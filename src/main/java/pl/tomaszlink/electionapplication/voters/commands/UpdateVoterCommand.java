package pl.tomaszlink.electionapplication.voters.commands;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateVoterCommand(
        @NotNull UUID id,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String pesel,
        @NotNull LocalDate birthDate) {
}
