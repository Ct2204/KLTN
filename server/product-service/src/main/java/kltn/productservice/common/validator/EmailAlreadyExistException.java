
package kltn.productservice.common.validator;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException (String message) {
        super(message);
    }
}
