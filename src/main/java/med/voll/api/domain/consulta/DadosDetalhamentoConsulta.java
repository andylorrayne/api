package med.voll.api.domain.consulta;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record DadosDetalhamentoConsulta(
    Long id,

    @NotNull
    Long idMedico,
    
    @NotNull
    Long idPaciente,
    
    @NotNull
    @Future
    LocalDateTime data ) {

    public DadosDetalhamentoConsulta(Consulta consulta) {
        this(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente().getId(), consulta.getData());
    }

    

}


