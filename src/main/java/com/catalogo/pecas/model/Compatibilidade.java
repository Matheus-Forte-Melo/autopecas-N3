package com.catalogo.pecas.model;

import jakarta.persistence.*;

@Entity
public class Compatibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "peca_id")
    private Peca peca;

    @ManyToOne
    @JoinColumn(name = "carro_id")
    private Carro carro;

    public Compatibilidade() {};

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Peca getPeca() {
        return peca;
    }
    public void setPeca(Peca peca) {
        this.peca = peca;
    }


    public Carro getCarro() {
        return carro;
    }
    public void setCarro(Carro carro) {
        this.carro = carro;
    }
}
