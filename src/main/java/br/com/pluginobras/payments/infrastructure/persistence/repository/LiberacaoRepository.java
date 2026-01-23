package br.com.pluginobras.payments.infrastructure.persistence.repository;

import br.com.pluginobras.payments.infrastructure.persistence.entity.LiberacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.projection.TotalLiberadoPorPrestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LiberacaoRepository extends JpaRepository<LiberacaoEntity, UUID> {
    @Query("""
        select l.prestadorId as prestadorId,
               sum(l.valor) as totalLiberado
        from LiberacaoEntity l
        group by l.prestadorId
    """)
    List<TotalLiberadoPorPrestador> totalLiberadoPorPrestador();
}
