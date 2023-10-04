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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.DadosListagemMedicos;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;

import org.springframework.web.bind.annotation.RequestBody;
@SecurityRequirement(name = "bearer-key")
@RestController
@RequestMapping("medicos")

public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        var medico = new Medico(dados);
        repository.save(medico);

        //uri criada pelo Spring
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }


    @GetMapping
    public ResponseEntity<Page<DadosListagemMedicos>> listar(@PageableDefault(size=10, sort = {"nome"}) Pageable paginacao){ //trata paginacao recurso do Spring
       var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedicos::new);

       return ResponseEntity.ok(page);
    }   


    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformações(dados);
        //sempre enviar informações com DTO que são os records
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));


    }

   /* 
    EXCLUSAO DEFINITIVA DO BANCO, NÃO É O SOLICITADO
    @DeleteMapping("/{id}")
    @Transactional
    public void excluir (@PathVariable Long id){
        repository.deleteById(id);
    }*/

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity desativar (@PathVariable Long id){ //reponseEntity classe do Spring que dá retorno dos protocolos http
        //metodo para apenas desativar o perfil do medico 
        // é necessario buscar o medico pelo id
        // foi criada uma coluna de ativos, após chamada, mudar o campo
        var medico = repository.getReferenceById(id);
        medico.desativar();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar (@PathVariable Long id){ 
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }



 
}