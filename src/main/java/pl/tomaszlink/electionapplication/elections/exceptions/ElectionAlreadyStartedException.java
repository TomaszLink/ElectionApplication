package pl.tomaszlink.electionapplication.elections.exceptions;

public class ElectionAlreadyStartedException extends RuntimeException {
    private static final String message = "Election already started.";
    public ElectionAlreadyStartedException() {
        super(message);
    }
}
