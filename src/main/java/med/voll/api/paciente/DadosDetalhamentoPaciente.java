package med.voll.api.paciente;

public record DadosDetalhamentoPaciente(
    Long id, 
    String nome, 
    String emai, 
    String cpf
) {
    public DadosDetalhamentoPaciente(Paciente paciente){
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf());
    }
}
