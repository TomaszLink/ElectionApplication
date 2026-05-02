package pl.tomaszlink.electionapplication.votes.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.tomaszlink.electionapplication.elections.exceptions.ElectionNotFoundException;
import pl.tomaszlink.electionapplication.model.*;
import pl.tomaszlink.electionapplication.voters.exceptions.VoterNotFoundException;
import pl.tomaszlink.electionapplication.votes.controllers.VoteController;

@RestControllerAdvice(assignableTypes = VoteController.class)
public class VoteExceptionHandler {

    @ExceptionHandler(VoteInElectionAlreadyExistsException.class)
    public ResponseEntity<VoteConflictErrorModel> handle(VoteInElectionAlreadyExistsException ex) {
        return ResponseEntity
                .status(409)
                .body(new VoteConflictErrorModel()
                        .error(VoteConflictErrorModel.ErrorEnum.VOTE_ALREADY_EXISTS)
                        .message(ex.getMessage()));
    }

    @ExceptionHandler(ElectionIsNotActiveException.class)
    public ResponseEntity<VoteConflictErrorModel> handle(ElectionIsNotActiveException ex) {
        return ResponseEntity
                .status(409)
                .body(new VoteConflictErrorModel()
                        .error(VoteConflictErrorModel.ErrorEnum.ELECTION_NOT_ACTIVE)
                        .message(ex.getMessage()));
    }

    @ExceptionHandler(VoterBlockedException.class)
    public ResponseEntity<VoterBlockedErrorModel> handle(VoterBlockedException ex) {
        return ResponseEntity
                .status(422)
                .body(new VoterBlockedErrorModel()
                        .error(VoterBlockedErrorModel.ErrorEnum.VOTER_BLOCKED)
                        .message(ex.getMessage()));
    }

    @ExceptionHandler({
            VoterNotFoundException.class,
            ElectionNotFoundException.class,
            ElectionOptionNotFoundException.class
    })
    public ResponseEntity<VoteResourceNotFoundErrorModel> handle(Exception ex) {
        VoteResourceNotFoundErrorModel.ErrorEnum errorEnum = switch (ex) {
            case VoterNotFoundException e ->
                    VoteResourceNotFoundErrorModel.ErrorEnum.VOTER_NOT_FOUND;
            case ElectionOptionNotFoundException e ->
                    VoteResourceNotFoundErrorModel.ErrorEnum.ELECTION_OPTION_NOT_FOUND;
            default ->
                    VoteResourceNotFoundErrorModel.ErrorEnum.ELECTION_NOT_FOUND;
        };

        return ResponseEntity
                .status(404)
                .body(new VoteResourceNotFoundErrorModel()
                        .error(errorEnum)
                        .message(ex.getMessage()));
    }

}
