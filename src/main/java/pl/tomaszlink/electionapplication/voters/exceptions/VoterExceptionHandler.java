package pl.tomaszlink.electionapplication.voters.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.tomaszlink.electionapplication.model.BadRequestErrorModel;
import pl.tomaszlink.electionapplication.model.VoterAlreadyExistsErrorModel;
import pl.tomaszlink.electionapplication.model.VoterNotFoundErrorModel;
import pl.tomaszlink.electionapplication.model.VoterTooYoungErrorModel;
import pl.tomaszlink.electionapplication.voters.controllers.VoterController;

@RestControllerAdvice(assignableTypes = VoterController.class)
@Log4j2
public class VoterExceptionHandler {

    @ExceptionHandler(VoterAlreadyExistsException.class)
    public ResponseEntity<VoterAlreadyExistsErrorModel> handle(VoterAlreadyExistsException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity
                .status(409)
                .body(new VoterAlreadyExistsErrorModel()
                        .error(VoterAlreadyExistsErrorModel.ErrorEnum.VOTER_ALREADY_EXISTS)
                        .message(ex.getMessage()));
    }

    @ExceptionHandler(VoterNotFoundException.class)
    public ResponseEntity<VoterNotFoundErrorModel> handle(VoterNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity
                .status(404)
                .body(new VoterNotFoundErrorModel()
                        .error(VoterNotFoundErrorModel.ErrorEnum.VOTER_NOT_FOUND)
                        .message(ex.getMessage()));
    }

    @ExceptionHandler(VoterTooYoungException.class)
    public ResponseEntity<VoterTooYoungErrorModel> handle(VoterTooYoungException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity
                .status(422)
                .body(new VoterTooYoungErrorModel()
                        .error(VoterTooYoungErrorModel.ErrorEnum.VOTER_TOO_YOUNG)
                        .message(ex.getMessage()));
    }
}
