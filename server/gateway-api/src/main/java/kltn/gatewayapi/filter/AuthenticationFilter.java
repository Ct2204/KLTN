package kltn.gatewayapi.filter;



import kltn.gatewayapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate template;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return handleErrorResponse(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    jwtUtil.validateToken(authHeader);

                } catch (Exception e) {
                    return handleErrorResponse(exchange, "Unauthorized access to application", HttpStatus.UNAUTHORIZED);
                   // throw new RuntimeException("un authorized access to application");


                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }

    private Mono<Void> handleErrorResponse(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        StringBuilder errorStatus = new StringBuilder();

        errorStatus.append("{");
        errorStatus.append("\"status\": \""+httpStatus.series().name()+"\",\n");
        errorStatus.append("    \"code\":" + httpStatus.value()+",\n");
        errorStatus.append("    \"message\": \""+errorMessage+"\"");
        errorStatus.append("}");


        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorStatus.toString().getBytes())));
    }
}