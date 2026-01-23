package br.com.pluginobras.payments.infrastructure.persistence.repository;

import br.com.pluginobras.payments.infrastructure.persistence.entity.DisputaEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.projection.TotalDisputaPorPrestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisputaRepository extends JpaRepository<DisputaEntity, UUID> {
    Optional<DisputaEntity> findByTransacaoId(String transacaoId);

    @Query("""
        select d.prestadorId as prestadorId,
               sum(d.valorDevolvidoCliente) as totalEmDisputa
        from DisputaEntity d
        where d.status = 'ABERTA'
        group by d.prestadorId
    """)
    List<TotalDisputaPorPrestador> totalEmDisputaPorPrestador();
}
