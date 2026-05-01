package pl.tomaszlink.electionapplication.voters.exceptions;

public class VoterAlreadyExistsException extends RuntimeException {
    private static final String message = "Voter with given PESEL already exists.";
    public VoterAlreadyExistsException() {
        super(message);
    }
}
