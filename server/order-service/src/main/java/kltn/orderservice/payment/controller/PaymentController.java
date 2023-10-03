package kltn.orderservice.payment.controller;


import kltn.orderservice.common.dto.ResponseDataDto;
import kltn.orderservice.common.dto.ResponseDto;
import kltn.orderservice.payment.service.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;



@RestController
@CrossOrigin({"*"})
@RequestMapping("/api/v1/order/payment")
public class PaymentController {
    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Autowired
    private PaymentService paymentService;

    @GetMapping({"/check-out-with-vnpay/{id}"})
    public ResponseEntity<ResponseDataDto> PaymentWithVnPay(@PathVariable("id") Long id, HttpServletRequest req) throws IOException {
        String url = this.paymentService.checkOutWithVnPay(req, id);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        responseDataDto.setStatus(HttpStatus.OK.series().name());
        responseDataDto.setCode(HttpStatus.OK.value());
        responseDataDto.setMessage("Checkout Successfully");
        responseDataDto.setData(url);
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    @GetMapping("/get/payment/info")
    public ResponseEntity<ResponseDto> getPaymentInfo(@RequestParam String vnp_ResponseCode, @RequestParam String vnp_TxnRef) {
        this.paymentService.ConfirmPayment(Long.valueOf(vnp_TxnRef), vnp_ResponseCode);
        ResponseDto response = new ResponseDto();
        response.setStatus(HttpStatus.OK.name());
        response.setCode(HttpStatus.OK.value());
        response.setMessage("your order be paid");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/check-out-with-pay-pal/{id}")
    public ResponseEntity<ResponseDataDto> PaymentWithPayPal(@PathVariable("id") Long id) throws PayPalRESTException {
        Payment payment = paymentService.createPayment(id);
        ResponseDataDto responseDataDto = new ResponseDataDto();
        for(Links link:payment.getLinks()) {
            if(link.getRel().equals("approval_url")) {
                responseDataDto.setStatus(HttpStatus.OK.series().name());
                responseDataDto.setCode(HttpStatus.OK.value());
                responseDataDto.setMessage("Checkout Successfully");
                responseDataDto.setData(link.getHref());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDataDto);
    }

    @GetMapping("/cancel")
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping( "/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) throws PayPalRESTException {
        Payment payment = paymentService.executePayment(paymentId, payerId);
        System.out.println(payment.toJSON());
        if (payment.getState().equals("approved")) {
            return "success";
        }
        return "fail";
    }
}
