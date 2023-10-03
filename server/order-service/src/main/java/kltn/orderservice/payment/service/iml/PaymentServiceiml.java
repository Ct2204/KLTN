package kltn.orderservice.payment.service.iml;

import kltn.orderservice.common.entity.OrderEntity;
import kltn.orderservice.common.exception.InternalServerErrorException;
import kltn.orderservice.common.exception.ResourceNotFoundException;
import kltn.orderservice.common.vo.OrderStatusE;
import kltn.orderservice.order.dto.OrderDto;
import kltn.orderservice.order.repository.OrderRepository;
import kltn.orderservice.order.service.OrderService;
import kltn.orderservice.payment.Config;
import kltn.orderservice.payment.PayPalConfig;
import kltn.orderservice.payment.service.PaymentService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentServiceiml implements PaymentService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @Autowired
    private APIContext apiContext;
    public String checkOutWithVnPay(HttpServletRequest req, Long orderId) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        OrderEntity order = this.orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order with id " + orderId + " not found!"));
        OrderDto orderDto = this.orderService.mapperOrderEntityToDto(order);
        long amount =orderDto.getTotalPrice().multiply(BigDecimal.valueOf(100)).longValue();
        String bankCode = req.getParameter("NCB");

        String vnp_TxnRef = String.valueOf(orderDto.getId());
        String vnp_IpAddr = Config.getIpAddress(req);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "other");

        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
       return paymentUrl;
    }
    public  void ConfirmPayment(Long id,String statusPayment) {
        if (statusPayment.equals("00")) {
            OrderEntity order = this.orderRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("order with id " + id + " not found!"));
            order.setStatus(OrderStatusE.PAID);
            this.orderRepository.save(order);
        }
        else throw  new InternalServerErrorException("payment cannot be processed");
    }
    public Payment createPayment(
            Long orderId) throws PayPalRESTException {
        OrderEntity order = this.orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order with id " + orderId + " not found!"));
        OrderDto orderDto = this.orderService.mapperOrderEntityToDto(order);
        Amount amount = new Amount();
        amount.setCurrency("USD");
      Double total = orderDto.getTotalPrice().divide(BigDecimal.valueOf(23000),2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription("Pay for Order"+orderId);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        String method ="paypal";
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        String intent = "sale";
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(PayPalConfig.cancelUrl);
        redirectUrls.setReturnUrl(PayPalConfig.sucessUrl);
        payment.setRedirectUrls(redirectUrls);
        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

}
