package ra.scurity.securrity.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ra.scurity.securrity.user_principal.UserPrincipal;

import java.util.Date;

@Component
public class JwtProvider {

    private Logger log= LoggerFactory.getLogger(JwtProvider.class);
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.expired-time}")
    private Long EXPIRED;


    //tạo chuỗi mã hóa access token
    public String generrateToken(UserPrincipal userPrincipal){
        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+EXPIRED))
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .compact();
    }
    //giải mã accessToken
    /**
     * @param token is generate by user anf SignatureAlgorithm.HS256
     * @return subject in token in username or email
     *
     * */
    public String parseToken(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJwt(token).getBody().getSubject();
    }

    //validate
    public boolean validateToken(String token){
        try {
           Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJwt(token);
        } catch (ExpiredJwtException e) {
            log.error("JWT: message error expired:", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT: message error unsupported:", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JWT: message error not formated:", e.getMessage());
        } catch (SignatureException e) {
            log.error("JWT: message error signature not math:", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT: message claims empty or argument invalid: ", e.getMessage());
        }
        return true;
    }

}
