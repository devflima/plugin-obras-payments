package br.com.pluginobras.payments.infrastructure.persistence.repository;

import br.com.pluginobras.payments.infrastructure.persistence.entity.DisputaEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.projection.TotalDisputaPorPrestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisputaRepository extends JpaRepository<DisputaEntity, UUID> {
    Optional<DisputaEntity> findByTransacaoId(UUID transacaoId);

    @Query("""
        select t.prestadorId as prestadorId,
               sum(d.valorDevolvidoCliente) as totalEmDisputa
        from DisputaEntity d
        join TransacaoEntity t
          on t.id = d.transacaoId
        where d.statusDisputa = 'ABERTA'
        group by t.prestadorId
    """)
    List<TotalDisputaPorPrestador> totalEmDisputaPorPrestador();
}
