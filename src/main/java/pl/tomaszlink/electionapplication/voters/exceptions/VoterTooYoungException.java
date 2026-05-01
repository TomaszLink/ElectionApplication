package pl.tomaszlink.electionapplication.voters.exceptions;

public class VoterTooYoungException extends RuntimeException {
    private static final String message = "Voter is too young to vote.";

    public VoterTooYoungException() {
        super(message);
    }
}
