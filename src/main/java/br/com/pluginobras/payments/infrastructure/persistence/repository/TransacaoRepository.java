package br.com.pluginobras.payments.infrastructure.persistence.repository;

import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoEntity, UUID> {
}
