

package kltn.productservice.security.jwt;

import kltn.productservice.common.exception.AuthorizeException;
import kltn.productservice.common.exception.InternalServerErrorException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private List<String> invalidatedTokens = new ArrayList<>();

    public void invalidateToken(String token) {
        invalidatedTokens.add(token.substring(7));
    }

    public int checkSizeTokenList() {
        return invalidatedTokens.size();
    }

    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }

    @Autowired
    private HttpServletResponse response;

    @Value("${kltn.jwtSecret}")
    private String jwtSecret;

    @Value("${kltn.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${kltn.jwtCookieName}")
    private String jwtCookie;

    public static String base64Encode(String token) {
        byte[] encodedBytes = Base64.encode(token.getBytes());
        return new String(encodedBytes, Charset.forName("UTF-8"));
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            Claims a= Jwts
                    .parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return a;
        } catch (Exception e) {
            // Handle JWT signature mismatch
            throw new AuthorizeException("Invalid JWT signature");
        }
    }


    public List<String> extractRolesFromToken(String token) {

        List<String> authorities = new ArrayList<>();
        try {
            Claims claims = extractAllClaims(token);
            List<String> roles = (List<String>) claims.get("roles");
            return roles;

        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi");
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
