package com.catalogo.pecas.repository;
import com.catalogo.pecas.model.Compatibilidade;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CompatibilidadeRepository extends JpaRepository<Compatibilidade, Long> {
    @Transactional
    void deleteByPeca_Id(Long peca_id);

    @Transactional
    void deleteByCarro_Id(Long carro_id);
}
