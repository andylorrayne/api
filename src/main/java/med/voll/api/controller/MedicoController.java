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
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosListagemMedicos;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("medicos")

public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        Medico medico = new Medico(dados);
        repository.save(medico);
    }


    @GetMapping
    public Page<DadosListagemMedicos> listar(@PageableDefault(size=10, sort = {"nome"}) Pageable paginacao){ //trata paginacao recurso do Spring
       return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedicos::new);
    }   


    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformações(dados);


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
    public void desativar (@PathVariable Long id){
        //metodo para apenas desativar o perfil do medico 
        // é necessario buscar o medico pelo id
        // foi criada uma coluna de ativos, após chamada, mudar o campo
        var medico = repository.getReferenceById(id);
        medico.desativar();
    }


 
}