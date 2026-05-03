package pl.tomaszlink.electionapplication.elections.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.tomaszlink.electionapplication.elections.controllers.ElectionController;
import pl.tomaszlink.electionapplication.model.BadRequestErrorModel;
import pl.tomaszlink.electionapplication.model.ElectionAlreadyStartedErrorModel;
import pl.tomaszlink.electionapplication.model.ElectionNotFoundErrorModel;

@RestControllerAdvice(assignableTypes = ElectionController.class)
@Log4j2
public class ElectionExceptionHandler {

    @ExceptionHandler(ElectionNotFoundException.class)
    public ResponseEntity<ElectionNotFoundErrorModel> handle(ElectionNotFoundException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity
                .status(404)
                .body(new ElectionNotFoundErrorModel()
                        .error(ElectionNotFoundErrorModel.ErrorEnum.ELECTION_NOT_FOUND)
                        .message(ex.getMessage()));
    }

    @ExceptionHandler(ElectionAlreadyStartedException.class)
    public ResponseEntity<ElectionAlreadyStartedErrorModel> handle(ElectionAlreadyStartedException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity
                .status(422)
                .body(new ElectionAlreadyStartedErrorModel()
                        .error(ElectionAlreadyStartedErrorModel.ErrorEnum.ELECTION_ALREADY_STARTED)
                        .message(ex.getMessage()));
    }

}
