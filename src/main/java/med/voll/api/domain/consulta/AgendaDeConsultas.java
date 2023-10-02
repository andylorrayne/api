package med.voll.api.domain.consulta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.controller.DadosCancelamentoConsulta;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    //Aplicação dos validadores
    @Autowired
    private List<ValidadorAgendamentoConsulta> validadores;


    public void agendar(DadosAgendamentoConsulta dados){

        //percorrendo a lista de validadores e aplicando as validações
        /**
         * estamos aplicando 3 principios do SOLID - S O D 
         * s - principio da responsabilidade unica, cada classe de validaçao tem sua responsabilidade
         * o - principio aberto-fechado
         * d - inversao de dependencias, ficamos dependendo de uma abstração
         */
        validadores.forEach(v -> v.validar(dados));

        if(!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidacaoException("Id informado do paciente não existe!");
        }

        if( dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("Id informado do medico não existe!");
        }

        var paciente = pacienteRepository.findById(dados.idPaciente()).get();
        var medico = escolherMedico(dados);
        var consulta = new Consulta(null, medico, paciente, dados.data(), null);

        consultaRepository.save(consulta);
    }


    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if(dados.especialidade() == null){
            throw new ValidacaoException("Especialidade obrigatoria quando medico não informado");
        }

        return medicoRepository.escolherMedicoAleatorioLivreData(dados.especialidade(), dados.data());
        

    }

    public void cancelar(DadosCancelamentoConsulta dados) {
    if (!consultaRepository.existsById(dados.idConsulta())) {
        throw new ValidacaoException("Id da consulta informado não existe!");
    }

    var consulta = consultaRepository.getReferenceById(dados.idConsulta());
    consulta.cancelar(dados.motivo());
}


    
}
