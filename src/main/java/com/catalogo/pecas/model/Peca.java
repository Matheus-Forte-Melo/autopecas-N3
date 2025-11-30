package com.catalogo.pecas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Peca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String categoria;
    private Double peso;
    private String descricao;

    @OneToMany(mappedBy = "peca", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Compatibilidade> compatibilidades;

    public Peca(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    };

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    };

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Compatibilidade> getCompatibilidades() {
        return compatibilidades;
    }

    public void setCompatibilidades(Set<Compatibilidade> compatibilidades) {
        this.compatibilidades = compatibilidades;
    }

    public String getIdsCarrosCompativeis() {
        if (compatibilidades == null) {
            return "";
        }
        return compatibilidades.stream().map(c -> c.getCarro().getId().toString()).collect(Collectors.joining(","));
    }

    public List<Long> getListIdsCarrosCompativeis() {
        if (this.compatibilidades == null) {
            return List.of();
        }

        return compatibilidades.stream().map(c -> c.getCarro().getId()).collect(Collectors.toList());
    }
}
