package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

/*
 * Utilizando o polimorfimo para deixar um código mais limpo e de fácil manutenção
 * E poderá ser aplicado os conceitos do SOLID 
 * Essa interface será usado pelo Spring Boot para chamar todas as classes que estão implentando como um lista de verificação
 * 
 */
public interface ValidadorAgendamentoConsulta {

    void validar(DadosAgendamentoConsulta dados);
    

}
