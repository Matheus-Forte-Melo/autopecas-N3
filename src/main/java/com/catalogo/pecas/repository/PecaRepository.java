package com.catalogo.pecas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.catalogo.pecas.model.Peca;

public interface PecaRepository extends JpaRepository<Peca, Long> {
}
