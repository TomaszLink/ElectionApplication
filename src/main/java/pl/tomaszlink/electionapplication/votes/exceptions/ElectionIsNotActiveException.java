package pl.tomaszlink.electionapplication.votes.exceptions;

public class ElectionIsNotActiveException extends RuntimeException {
    private static final String message = "Election is not active.";
    public ElectionIsNotActiveException() {
        super(message);
    }
}
