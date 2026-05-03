package pl.tomaszlink.electionapplication.voters.commands;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterVoterCommand(
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String pesel,
        @NotNull LocalDate birthDate) {
}
