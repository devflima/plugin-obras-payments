package br.com.pluginobras.payments.infrastructure.persistence.repository;

import br.com.pluginobras.payments.infrastructure.persistence.entity.ContaEscrowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContaEscrowRepository extends JpaRepository<ContaEscrowEntity, UUID> {
    Optional<ContaEscrowEntity> findByTransacaoId(String transacaoId);
}
