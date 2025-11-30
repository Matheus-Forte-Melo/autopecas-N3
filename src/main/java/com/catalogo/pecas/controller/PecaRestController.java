package com.catalogo.pecas.controller;

import com.catalogo.pecas.model.Carro;
import com.catalogo.pecas.model.Compatibilidade;
import com.catalogo.pecas.model.Peca;
import com.catalogo.pecas.repository.CarroRepository;
import com.catalogo.pecas.repository.CompatibilidadeRepository;
import com.catalogo.pecas.repository.PecaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pecas")
@Tag(name = "Peças", description = "Endpoints para gerenciamento completo de peças e suas compatibilidades")
public class PecaRestController {

    @Autowired
    private PecaRepository pecaRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private CompatibilidadeRepository compatibilidadeRepository;

    @GetMapping
    @Operation(summary = "Listar todas as peças", description = "Retorna uma lista com todas as peças cadastradas no sistema.")
    public List<Peca> listarTodas() {
        return pecaRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar peça por ID", description = "Retorna os detalhes de uma peça específica baseada no seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Peça encontrada"),
            @ApiResponse(responseCode = "404", description = "Peça não encontrada")
    })
    public ResponseEntity<Peca> buscarPorId(@Parameter(description = "ID da peça a ser buscada") @PathVariable Long id) {
        return pecaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Criar nova peça", description = "Cadastra uma nova peça e cria automaticamente o vínculo de compatibilidade com os carros informados.")
    public Peca criar(@RequestBody PecaInput input) {
        Peca peca = new Peca();
        peca.setNome(input.nome);
        peca.setCategoria(input.categoria);
        peca.setPeso(input.peso);
        peca.setDescricao(input.descricao);

        Peca pecaSalva = pecaRepository.save(peca);

        if (input.carrosIds != null && !input.carrosIds.isEmpty()) {
            for (Long carroId : input.carrosIds) {
                Carro carro = carroRepository.findById(carroId).orElse(null);
                if (carro != null) {
                    Compatibilidade comp = new Compatibilidade();
                    comp.setPeca(pecaSalva);
                    comp.setCarro(carro);
                    compatibilidadeRepository.save(comp);
                }
            }
        }
        return pecaSalva;
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualizar peça", description = "Atualiza os dados de uma peça existente e redefine suas compatibilidades com carros.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Peça atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Peça não encontrada para atualização")
    })
    public ResponseEntity<Peca> atualizar(@Parameter(description = "ID da peça a ser atualizada") @PathVariable Long id, @RequestBody PecaInput input) {
        return pecaRepository.findById(id)
                .map(peca -> {
                    peca.setNome(input.nome);
                    peca.setCategoria(input.categoria);
                    peca.setPeso(input.peso);
                    peca.setDescricao(input.descricao);

                    Peca pecaAtualizada = pecaRepository.save(peca);

                    if (input.carrosIds != null) {
                        List<Compatibilidade> antigas = compatibilidadeRepository.findAll();
                        antigas.stream()
                                .filter(c -> c.getPeca().getId().equals(id))
                                .forEach(compatibilidadeRepository::delete);

                        for (Long carroId : input.carrosIds) {
                            Carro carro = carroRepository.findById(carroId).orElse(null);
                            if (carro != null) {
                                Compatibilidade comp = new Compatibilidade();
                                comp.setPeca(pecaAtualizada);
                                comp.setCarro(carro);
                                compatibilidadeRepository.save(comp);
                            }
                        }
                    }

                    return ResponseEntity.ok(pecaAtualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Deletar peça", description = "Remove uma peça do sistema e suas compatibilidades associadas (via cascade).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Peça deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Peça não encontrada")
    })
    public ResponseEntity<Void> deletar(@Parameter(description = "ID da peça a ser removida") @PathVariable Long id) {
        if (pecaRepository.existsById(id)) {
            pecaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Schema(description = "Objeto de transferência de dados para criação e atualização de peças")
    public static class PecaInput {
        @Schema(description = "Nome da peça", example = "Amortecedor Dianteiro")
        public String nome;

        @Schema(description = "Categoria da peça", example = "Suspensão")
        public String categoria;

        @Schema(description = "Peso em kg", example = "2.5")
        public Double peso;

        @Schema(description = "Descrição detalhada", example = "Amortecedor a gás para linha passeio")
        public String descricao;

        @Schema(description = "Lista de IDs dos carros compatíveis", example = "[1, 2, 5]")
        public List<Long> carrosIds;
    }
}