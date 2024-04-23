package ra.scurity.advice;

import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ra.scurity.exception.DataExist;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApplicationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidData(MethodArgumentNotValidException e) {
        Map<String, String> error = new HashMap<>();
        e.getFieldErrors().forEach((err) -> {
            error.put(err.getField(), err.getDefaultMessage());
        });
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(DataExist.class)
    public ResponseEntity<?> handleDataExist(DataExist e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> NoSuchElementException(NoSuchElementException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }



}
