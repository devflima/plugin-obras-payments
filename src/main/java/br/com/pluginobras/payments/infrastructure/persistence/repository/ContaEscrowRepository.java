package br.com.pluginobras.payments.infrastructure.persistence.repository;

import br.com.pluginobras.payments.infrastructure.persistence.entity.ContaEscrowEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.projection.TotalRetidoPorPrestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContaEscrowRepository extends JpaRepository<ContaEscrowEntity, UUID> {
    Optional<ContaEscrowEntity> findByTransacaoId(UUID transacaoId);

    @Query("""
        select e.prestadorId as prestadorId,
               sum(e.saldoRetido) as totalRetido
        from ContaEscrowEntity e
        group by e.prestadorId
    """)
    List<TotalRetidoPorPrestador> totalRetidoPorPrestador();
}
