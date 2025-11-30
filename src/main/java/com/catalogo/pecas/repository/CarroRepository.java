package com.catalogo.pecas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catalogo.pecas.model.Carro;

public interface CarroRepository extends JpaRepository<Carro, Long> {
}
