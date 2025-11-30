package com.catalogo.pecas.controller;

import com.catalogo.pecas.model.Carro;
import com.catalogo.pecas.repository.CarroRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/carros")
@Tag(name = "Carros", description = "Endpoints para gestão de veículos")
public class CarroRestController {

    @Autowired
    private CarroRepository carroRepository;

    @GetMapping
    @Operation(summary = "Listar todos os carros", description = "Retorna a lista completa de carros cadastrados.")
    public List<Carro> showAll() {
        return carroRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar carro por ID", description = "Retorna os detalhes de um carro específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carro encontrado"),
            @ApiResponse(responseCode = "404", description = "Carro não encontrado")
    })
    public ResponseEntity<Carro> showById(@Parameter(description = "ID do carro") @PathVariable Long id) {
        return carroRepository.findById(id)
                .map(carro -> ResponseEntity.ok().body(carro))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo carro", description = "Cadastra um novo veículo no sistema.")
    public Carro create(@RequestBody CarroInput input) {
        Carro carro = new Carro();
        carro.setNome(input.nome);
        carro.setMarca(input.marca);
        carro.setAno(input.ano);
        return carroRepository.save(carro);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar carro", description = "Atualiza os dados de um veículo existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro não encontrado")
    })
    public ResponseEntity<Carro> update(@Parameter(description = "ID do carro") @PathVariable Long id, @RequestBody CarroInput input) {
        return carroRepository.findById(id).map(carroAlvo -> {
            carroAlvo.setMarca(input.marca);
            carroAlvo.setNome(input.nome);
            carroAlvo.setAno(input.ano);
            Carro carroAlvoSalvo = carroRepository.save(carroAlvo);
            return ResponseEntity.ok().body(carroAlvoSalvo);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar carro", description = "Remove um veículo do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro não encontrado")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID do carro") @PathVariable Long id) {
        if (carroRepository.existsById(id)) {
            carroRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Schema(description = "Objeto de transferência de dados para criação e atualização de carros")
    public static class CarroInput {
        @Schema(description = "Modelo do carro", example = "Uno")
        public String nome;

        @Schema(description = "Marca do fabricante", example = "Fiat")
        public String marca;

        @Schema(description = "Ano de fabricação", example = "2010")
        public int ano;
    }
}