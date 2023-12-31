package med.voll.api.domain.consulta.validacoes.agendamento;

import java.time.DayOfWeek;

import org.springframework.stereotype.Component;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.infra.exception.ValidacaoException;

@Component
public class ValidacaoHoraFuncionamentoClinica implements ValidadorAgendamentoConsulta{

    public void validar(DadosAgendamentoConsulta dados){
        var dataConsulta = dados.data();

        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);

        var antesDaAberturaClinica = dataConsulta.getHour() <7;

        var depoiDEoEncerramentoClinica = dataConsulta.getHour() >18;

        if(domingo || antesDaAberturaClinica || depoiDEoEncerramentoClinica){
            throw new ValidacaoException("Consulta fora do horario de funcionamento da clinica");
        }
    }
    
}
