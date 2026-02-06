package br.com.pluginobras.payments.infrastructure.persistence.repository;

import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoEntity, UUID>, JpaSpecificationExecutor<TransacaoEntity> {
    List<TransacaoEntity> findByStatusTransacao(String statusTransacao);
}
