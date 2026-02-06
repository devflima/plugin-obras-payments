package br.com.pluginobras.payments.infrastructure.persistence.entity;

import br.com.pluginobras.payments.domain.model.disputa.DisputaManual;
import br.com.pluginobras.payments.domain.model.disputa.StatusDisputa;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "disputa_manual")
@Data
public class DisputaEntity {

    @Id
    private UUID transacaoId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "transacao_id")
    private TransacaoEntity transacao;

    private BigDecimal valorDevolvidoCliente;
    private String motivo;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataResolucao;

    @Enumerated(EnumType.STRING)
    private StatusDisputa statusDisputa;

    protected DisputaEntity() {}

    public static DisputaEntity fromDomain(DisputaManual domain, TransacaoEntity transacaoEntity) {
        DisputaEntity d = new DisputaEntity();

        d.transacao = transacaoEntity;

        d.valorDevolvidoCliente = domain.getValorDevolvidoCliente();
        d.motivo = domain.getMotivo();
        d.dataAbertura = domain.getDataAbertura();
        d.dataResolucao = domain.getDataResolucao();
        d.statusDisputa = domain.getStatusDisputa();

        return d;
    }

    public DisputaManual toDomain() {
        DisputaManual d = new DisputaManual(
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

    public void applyStateFromDomain(DisputaManual domain) {
        this.statusDisputa = domain.getStatusDisputa();
        this.dataResolucao = domain.getDataResolucao();
    }

}
