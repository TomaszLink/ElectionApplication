package pl.tomaszlink.electionapplication.votes.exceptions;

public class VoteInElectionAlreadyExistsException extends RuntimeException {
    private static final String message = "Vote in election already exists.";
    public VoteInElectionAlreadyExistsException() {
        super(message);
    }
}
