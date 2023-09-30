package kltn.userservice.security.jwt;


import kltn.userservice.common.entity.UserRefreshTokenEntity;
import kltn.userservice.common.exception.AuthorizeException;
import kltn.userservice.common.exception.ResourceNotFoundException;
import kltn.userservice.common.exception.TokenRefreshException;
import kltn.userservice.user.repository.UserRefreshTokenRepository;
import kltn.userservice.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@Component
public class AuthRefreshToken {

    @Value("${backend.security.jwt.RefreshExpirationMs}")
    private Long jwtRefreshExpirationMs;


    @Value("${kltn.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${kltn.jwtCookieName}")
    private String jwtCookie;

    @Value("${backend.security.jwt.privateKey}")
    private String privateKey;

    @Autowired
    private UserRefreshTokenRepository userRefreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;


    public boolean verifyRefreshToken(String refreshToken){
        UserRefreshTokenEntity userRefreshToken = userRefreshTokenRepository.findByToken(refreshToken).orElseThrow(
                ()-> new ResourceNotFoundException("Can not find user refresh token from your token")
        );
        if(userRefreshToken.getExpiryDate().compareTo(Instant.now())<0){
            userRefreshTokenRepository.delete(userRefreshToken);
            throw new TokenRefreshException(
                    userRefreshToken.getToken(),"Refresh token was expired. Please make a new signIn request");
        }
        return true;
    }
    @Transactional
    public int deleteRefreshTokenByUserId(Long user_id){
        return userRefreshTokenRepository.deleteByUser(userRepository.findById(user_id).get());
    }
    public String generateRefreshToken(String userName)   {

        UserRefreshTokenEntity userRefreshToken = new UserRefreshTokenEntity();
        userRefreshToken.setUser(userRepository.findByEmail(userName).get());
        userRefreshToken.setExpiryDate(Instant.now().plusMillis(jwtRefreshExpirationMs));

        userRefreshToken.setToken(buildToken(new HashMap<>(),userName,jwtRefreshExpirationMs));

        userRefreshTokenRepository.save(userRefreshToken);

        return buildToken(new HashMap<>(),userName,jwtRefreshExpirationMs);
    }

    public String buildToken(
            Map<String,Object> extraClaims,
            String userName,
            long expiration
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(signInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String getRefreshTokenFromCookie(Cookie[] cookies){
        String refreshToken=null;
        if(cookies!=null){
            Optional<Cookie> cookieOptional= Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("refresh_token"))
                    .findFirst();
            if(cookieOptional.isPresent()){
                refreshToken=cookieOptional.get().getValue();
            }
        }
        return refreshToken;
    }
    private Key signInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
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
            return Jwts
                    .parserBuilder()
                    .setSigningKey(signInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // Handle JWT signature mismatch
            throw new AuthorizeException("Invalid JWT signature");
        }
    }

}
