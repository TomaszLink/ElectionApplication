package pl.tomaszlink.electionapplication.votes.exceptions;

public class VoterBlockedException extends RuntimeException {
    public VoterBlockedException(String message) {
        super(message);
    }
}
