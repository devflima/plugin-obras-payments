package br.com.pluginobras.payments.infrastructure.persistence.repository;

import br.com.pluginobras.payments.infrastructure.persistence.entity.DisputaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DisputaRepository extends JpaRepository<DisputaEntity, UUID> {
}
