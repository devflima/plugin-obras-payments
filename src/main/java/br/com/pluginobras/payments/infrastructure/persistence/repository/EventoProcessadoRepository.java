package br.com.pluginobras.payments.infrastructure.persistence.repository;

import br.com.pluginobras.payments.infrastructure.persistence.entity.EventoProcessadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventoProcessadoRepository extends JpaRepository<EventoProcessadoEntity, UUID> {
}
