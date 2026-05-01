package pl.tomaszlink.electionapplication.global;

import jakarta.persistence.OptimisticLockException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
}
