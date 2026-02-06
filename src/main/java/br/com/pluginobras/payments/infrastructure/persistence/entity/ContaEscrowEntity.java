package br.com.pluginobras.payments.infrastructure.persistence.entity;

import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.escrow.StatusEscrow;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "conta_escrow")
public class ContaEscrowEntity {

    @Id
    private UUID transacaoId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "transacao_id")
    private TransacaoEntity transacao;

    private String prestadorId;

    private BigDecimal saldoRetido;

    private String statusEscrow;

    protected ContaEscrowEntity() {}

    public static ContaEscrowEntity fromDomain(ContaEscrow domain, TransacaoEntity transacaoEntity) {
        ContaEscrowEntity c = new ContaEscrowEntity();

        c.transacao = transacaoEntity;

        c.prestadorId = domain.getPrestadorId();
        c.saldoRetido = domain.getSaldoRetido();
        c.statusEscrow = domain.getStatusEscrow().name();

        return c;
    }

    public ContaEscrow toDomain() {
        ContaEscrow escrow = new ContaEscrow(
                this.transacaoId,
                this.prestadorId,
                this.saldoRetido
        );

        escrow.reconstruirEstado(StatusEscrow.valueOf(this.statusEscrow));

        return escrow;
    }

    public void applyStateFromDomain(ContaEscrow domain) {
        this.saldoRetido = domain.getSaldoRetido();
        this.statusEscrow = domain.getStatusEscrow().name();
    }

}
