package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.consulta.validacoes.cancelamento.MotivoCancelamento;

public record DadosCancelamentoConsulta(
    @NotNull
    Long idConsulta,

    @NotNull
    MotivoCancelamento motivo) {

}
