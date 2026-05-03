package pl.tomaszlink.electionapplication.global;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.tomaszlink.electionapplication.model.BadRequestErrorModel;
import pl.tomaszlink.electionapplication.model.ErrorModel;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    private static final String ANOTHER_TRANSACTION_MESSAGE = "Resource was modified by another user. Please refresh data and try again.";

    @ExceptionHandler({
            ObjectOptimisticLockingFailureException.class,
            OptimisticLockException.class
    })
    public ResponseEntity<ErrorModel> handleOptimisticLockingException(Exception ex) {
        log.warn(ANOTHER_TRANSACTION_MESSAGE);
        return ResponseEntity
                .status(409)
                .body(new ErrorModel().error("CONFLICT")
                        .message(ANOTHER_TRANSACTION_MESSAGE)
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BadRequestErrorModel> handle(ConstraintViolationException ex) {
        return ResponseEntity
                .status(400)
                .body(new BadRequestErrorModel()
                        .error(BadRequestErrorModel.ErrorEnum.BAD_REQUEST)
                        .message(ex.getMessage()));
    }
}
