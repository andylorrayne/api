package med.voll.api.domain.paciente;

public record DadosListagemPacientes(Long id, String nome, String emai, String cpf) {
     public DadosListagemPacientes( Paciente paciente){
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf());
     }    
}
