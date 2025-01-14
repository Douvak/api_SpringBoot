package med.vall.api.domain.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.vall.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {
    @Value("api.security.token.secret")
    private String secret;
    public String gerarToken(Usuario usuario){
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("PI Voll.med")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao cherar o token", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            System.out.println("string token = " + tokenJWT);
            var algoritmo = Algorithm.HMAC256(secret);
            var user = JWT.require(algoritmo)
                    .withIssuer("API Voll.med")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
            System.out.println(user);
            return user;
        } catch (JWTVerificationException exception){

           throw new RuntimeException("token invalido ou expirado,", exception);
        }
    }

    private Instant dataExpiracao() {

        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
