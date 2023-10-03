package kltn.orderservice.payment.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;

public interface PaymentService {
    public String checkOutWithVnPay(HttpServletRequest req, Long orderId) throws UnsupportedEncodingException;
    public  void ConfirmPayment(Long id,String statusPayment);
    public Payment createPayment(
            Long orderId) throws PayPalRESTException;
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
