package med.voll.api.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;


/* Essa classe vai auxiliar a desempenhar o papel de intercptar as requisições e fazer a validação de autenticação */


//notação generica indicando um componente Spring para carregar 
@Component
public class SecurityFilter extends OncePerRequestFilter{

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    //o filtro vai ser aplicado uma vez por requisição
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                
        //ler token e validar token
                
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null){
            var subject = tokenService.getSubject(tokenJWT); //consegue buscar o usuario que gerou o token

            var usuario = repository.findByLogin(subject);

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } 
        
        


        //até aqui o filtro está informando que existe autenticacao"

        filterChain.doFilter(request, response);
       
    }


    //pegando o cabeçalho da requisicao
    private String recuperarToken(HttpServletRequest request) {
        var autorizacaoCabecalho = request.getHeader("Authorization");

        if (autorizacaoCabecalho != null){
            return autorizacaoCabecalho.replace("Bearer ", "");
        }

        return null;

        
    }
    

}
