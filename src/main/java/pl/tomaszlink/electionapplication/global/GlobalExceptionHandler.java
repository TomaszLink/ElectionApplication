package pl.tomaszlink.electionapplication.global;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.tomaszlink.electionapplication.model.BadRequestErrorModel;
import pl.tomaszlink.electionapplication.model.ErrorModel;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ObjectOptimisticLockingFailureException.class,
            OptimisticLockException.class
    })
    public ResponseEntity<ErrorModel> handleOptimisticLockingException(Exception exception) {
        return ResponseEntity
                .status(409)
                .body(new ErrorModel().error("CONFLICT").message(
                        "Resource was modified by another user. Please refresh data and try again.")
                );
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
