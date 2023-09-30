package med.voll.api.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //esse é o metodo que fará a consulta do usuário no banco de dados
    UserDetails findByLogin(String login);
    
}
