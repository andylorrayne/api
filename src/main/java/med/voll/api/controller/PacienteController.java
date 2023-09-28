package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.DadosDetalhamentoMedico;
import med.voll.api.paciente.DadosAtualizarPaciente;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.DadosDetalhamentoPaciente;
import med.voll.api.paciente.DadosListagemPacientes;
import med.voll.api.paciente.Paciente;
import med.voll.api.paciente.PacienteRepository;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("pacientes")
public class PacienteController {
    
    @Autowired
    private PacienteRepository repository;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
        var paciente = new Paciente(dados);
        repository.save(paciente);

        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente))
        
    }

    @GetMapping
    public ResponseEntity< Page<DadosListagemPacientes>> listar (@PageableDefault(size=10, sort = {"nome"}) Pageable paginacao){
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemPacientes::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizarPaciente dados){
        
        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformações(dados);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(null));


    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity desativar (@PathVariable Long id){
        //metodo para apenas desativar o perfil do medico 
        // é necessario buscar o medico pelo id
        // foi criada uma coluna de ativos, após chamada, mudar o campo
        var paciente = repository.getReferenceById(id);
        paciente.desativar();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar (@PathVariable Long id){ 
        var paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }


}