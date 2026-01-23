package br.com.pluginobras.payments.infrastructure.persistence.entity;

import br.com.pluginobras.payments.domain.model.disputa.DisputaManual;
import br.com.pluginobras.payments.domain.model.disputa.StatusDisputa;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "disputa")
@Data
public class DisputaEntity {

    @Id
    private UUID id;

    private String transacaoId;
    private BigDecimal valorDevolvidoCliente;
    private String motivo;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataResolucao;
    private StatusDisputa statusDisputa;

    protected DisputaEntity() {}

    public static DisputaEntity fromDomain(DisputaManual domain) {
        DisputaEntity d = new DisputaEntity();

        d.id = domain.getId();
        d.transacaoId = domain.getTransacaoId();
        d.valorDevolvidoCliente = domain.getValorDevolvidoCliente();
        d.motivo = domain.getMotivo();
        d.dataAbertura = domain.getDataAbertura();
        d.dataResolucao = domain.getDataResolucao();
        d.statusDisputa = domain.getStatusDisputa();

        return d;
    }

    public DisputaManual toDomain() {
        DisputaManual d = new DisputaManual(
                this.id,
                this.transacaoId,
                this.valorDevolvidoCliente,
                this.motivo
        );

        d.recontruirEstado(
                StatusDisputa.valueOf(this.statusDisputa.name()),
                this.dataAbertura,
                this.dataResolucao
        );

        return d;
    }

}
