package br.com.pluginobras.payments.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "webhook_evento_processado")
@Data
public class EventoProcessadoEntity {

    @Id
    private UUID idEvento;
    private LocalDateTime dataProcessamento;

    protected EventoProcessadoEntity() {}

    public EventoProcessadoEntity (LocalDateTime dataProcessamento) {
        this.idEvento = UUID.randomUUID();
        this.dataProcessamento = dataProcessamento;
    }

}
