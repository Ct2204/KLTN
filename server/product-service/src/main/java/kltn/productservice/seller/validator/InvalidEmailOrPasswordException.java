
package kltn.productservice.seller.validator;

public class InvalidEmailOrPasswordException extends RuntimeException{

    public InvalidEmailOrPasswordException(String message) {
        super(message);
    }
}
