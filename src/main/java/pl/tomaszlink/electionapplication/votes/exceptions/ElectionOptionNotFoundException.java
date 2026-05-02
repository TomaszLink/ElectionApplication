package pl.tomaszlink.electionapplication.votes.exceptions;

public class ElectionOptionNotFoundException extends RuntimeException {
    public ElectionOptionNotFoundException(String message) {
        super(message);
    }
}
