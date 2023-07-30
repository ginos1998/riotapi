package riot.riotapi.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExeptionsHandler {

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<String> handleServiceExepcion(ServiceException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(exception.getMessage());
  }

}
