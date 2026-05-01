package pl.tomaszlink.electionapplication.elections.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.tomaszlink.electionapplication.elections.controllers.ElectionController;
import pl.tomaszlink.electionapplication.model.BadRequestErrorModel;
import pl.tomaszlink.electionapplication.model.ElectionAlreadyStartedErrorModel;
import pl.tomaszlink.electionapplication.model.ElectionNotFoundErrorModel;

@RestControllerAdvice(assignableTypes = ElectionController.class)
public class ElectionExceptionHandler {

    @ExceptionHandler(ElectionNotFoundException.class)
    public ResponseEntity<ElectionNotFoundErrorModel> handle(ElectionNotFoundException ex) {
        return ResponseEntity
                .status(404)
                .body(new ElectionNotFoundErrorModel()
                        .error(ElectionNotFoundErrorModel.ErrorEnum.ELECTION_NOT_FOUND)
                        .message(ex.getMessage()));
    }

    @ExceptionHandler(ElectionAlreadyStartedException.class)
    public ResponseEntity<ElectionAlreadyStartedErrorModel> handle(ElectionAlreadyStartedException ex) {
        return ResponseEntity
                .status(422)
                .body(new ElectionAlreadyStartedErrorModel()
                        .error(ElectionAlreadyStartedErrorModel.ErrorEnum.ELECTION_ALREADY_STARTED)
                        .message(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BadRequestErrorModel> handle(ConstraintViolationException e) {
        return ResponseEntity
                .status(400)
                .body(new BadRequestErrorModel()
                        .error(BadRequestErrorModel.ErrorEnum.BAD_REQUEST)
                        .message(e.getMessage()));
    }
}
