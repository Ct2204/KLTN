
package kltn.orderservice.common.exception;

import kltn.orderservice.common.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus("BAD_REQUEST");
        responseDto.setCode(HttpStatus.BAD_REQUEST.value());

        List<String> errorMessages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String defaultMessage = error.getDefaultMessage();
            errorMap.put(field, defaultMessage);
            errorMessages.add(defaultMessage);
        });

        if (errorMessages.size() == 1) {
            responseDto.setMessage(errorMessages.get(0));
        } else {
            responseDto.setMessage(errorMessages);
        }

        return responseDto;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ResponseDto response = new ResponseDto();
        response.setStatus("BAD_REQUEST");
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseDto response = new ResponseDto();
        response.setStatus(HttpStatus.NOT_FOUND.series().name());
        response.setCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ResponseDto> handleDuplicateResourceException(DuplicateResourceException ex) {
        ResponseDto response = new ResponseDto();
        response.setStatus(HttpStatus.CONFLICT.series().name());
        response.setCode(HttpStatus.CONFLICT.value());
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ResponseDto> handleInternalServerErrorException(InternalServerErrorException ex) {
        ResponseDto response = new ResponseDto();
        response.setStatus("ERR_BAD_REQUEST");
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(AuthorizeException.class)
    public ResponseEntity<ResponseDto> handleAuthorizeException(AuthorizeException ex) {
        ResponseDto response = new ResponseDto();
        response.setStatus("FORBIDDEN");
        response.setCode(403);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(NotActivatedExceptionHandler.class)
    public ResponseEntity<ResponseDto> handleNotActivatedExceptionHandler(NotActivatedExceptionHandler ex) {
        ResponseDto response = new ResponseDto();
        response.setStatus("NOT_ACTIVATED");
        response.setCode(403);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ResponseDto> handleTokenRefreshException(TokenRefreshException ex){
        ResponseDto response = new ResponseDto();
        response.setStatus(HttpStatus.UNAUTHORIZED.toString());
        response.setCode(401);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
