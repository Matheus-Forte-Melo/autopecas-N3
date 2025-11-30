package com.catalogo.pecas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Carro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String marca;
    private int ano;

    @OneToMany(mappedBy = "carro")
    @JsonIgnore
    private Set<Compatibilidade> compatibilidades;

    public Carro() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    };

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Set<Compatibilidade> getCompatibilidades() {
        return compatibilidades;
    }

    public void setCompatibilidades(Set<Compatibilidade> compatibilidades) {
        this.compatibilidades = compatibilidades;
    }

    public String getIdsPecasCompativeis() {
        if (compatibilidades == null || compatibilidades.isEmpty()) {
            return "";
        }
        return compatibilidades.stream()
                .map(c -> c.getPeca().getId().toString())
                .collect(Collectors.joining(","));
    }
}
