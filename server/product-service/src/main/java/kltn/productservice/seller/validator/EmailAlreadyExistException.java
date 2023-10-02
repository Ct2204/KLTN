
package kltn.productservice.seller.validator;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException (String message) {
        super(message);
    }
}
