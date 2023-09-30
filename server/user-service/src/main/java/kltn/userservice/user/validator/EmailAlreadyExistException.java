package kltn.userservice.user.validator;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException (String message) {
        super(message);
    }
}
