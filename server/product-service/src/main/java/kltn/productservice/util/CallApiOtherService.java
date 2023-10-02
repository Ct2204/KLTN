package kltn.productservice.util;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CallApiOtherService {

    @Autowired
    private HttpServletRequest request;

    public String getToken(){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return token;
    }


}
