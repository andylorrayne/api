package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.DadosAtualizacaoMedico;
import med.voll.api.paciente.DadosAtualizarPaciente;
import med.voll.api.paciente.DadosCadastroPaciente;
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
    public void cadastrar(@RequestBody @Valid DadosCadastroPaciente dados) {
        Paciente paciente = new Paciente(dados);
        repository.save(paciente);
        
    }

    @GetMapping
    public Page<DadosListagemPacientes> listar (@PageableDefault(size=10, sort = {"nome"}) Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemPacientes::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizarPaciente dados){
        
        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformações(dados);


    }

    @DeleteMapping("/{id}")
    @Transactional
    public void desativar (@PathVariable Long id){
        //metodo para apenas desativar o perfil do medico 
        // é necessario buscar o medico pelo id
        // foi criada uma coluna de ativos, após chamada, mudar o campo
        var paciente = repository.getReferenceById(id);
        paciente.desativar();
    }


}