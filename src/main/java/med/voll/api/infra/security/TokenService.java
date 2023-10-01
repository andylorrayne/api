package med.voll.api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import med.voll.api.domain.usuario.Usuario;

@Service
public class TokenService {

    public String gerarToken(Usuario usuario){
        /* o codigo foi fornecido no gitHub da biblioteca, apenas foi feita algumas alterações */
        try {
            var algoritmo = Algorithm.HMAC256("senha");
            return JWT.create()
                .withIssuer("API Voll.med")
                .withSubject(usuario.getLogin())
                .withExpiresAt(dataExpiracao())
                .sign(algoritmo);
            
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

   
        /*metodo que determina o tempo em que o token será valido
         * nesse caso será pego a hora atual e somada por mais duas horas
         * Instant é a biblioteca do JAVA 8 que trata horas 
         */

        
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
