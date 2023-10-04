package med.voll.api.domain.consulta.validacoes.cancelamento;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

public interface ValidadorCancelamentoDeConsulta {

    void validar(DadosAgendamentoConsulta dados);

}
