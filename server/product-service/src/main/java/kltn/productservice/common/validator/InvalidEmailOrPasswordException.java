
package kltn.productservice.common.validator;

public class InvalidEmailOrPasswordException extends RuntimeException{

    public InvalidEmailOrPasswordException(String message) {
        super(message);
    }
}
