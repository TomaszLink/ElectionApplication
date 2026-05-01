package pl.tomaszlink.electionapplication.voters.models;

import java.time.LocalDate;

public record UpdateVoterCommand(String firstName, String lastName, String pesel, LocalDate birthDate) {
}
