
package kltn.userservice.user.validator;

public class InvalidEmailOrPasswordException extends RuntimeException{

    public InvalidEmailOrPasswordException(String message) {
        super(message);
    }
}
