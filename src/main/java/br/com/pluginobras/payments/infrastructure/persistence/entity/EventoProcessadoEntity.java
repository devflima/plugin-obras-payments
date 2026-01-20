package br.com.pluginobras.payments.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "webhook_evento_processado")
public class EventoProcessadoEntity {

    @Id
    private String idEvento;
    private LocalDateTime dataProcessamento;

    protected EventoProcessadoEntity() {}

    public EventoProcessadoEntity (String idEvento, LocalDateTime dataProcessamento) {
        this.idEvento = idEvento;
        this.dataProcessamento = dataProcessamento;
    }

}
