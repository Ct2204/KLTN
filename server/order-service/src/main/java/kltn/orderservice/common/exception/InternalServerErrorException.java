
package kltn.orderservice.common.exception;

public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String msg) {
        super(msg);
    }
}
