package br.com.pluginobras.payments.infrastructure.persistence.entity;

import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.escrow.StatusEscrow;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "conta_escrow")
public class ContaEscrowEntity {

    @Id
    private UUID id;

    private String transacaoId;
    private String prestadorId;

    private BigDecimal saldoRetido;

    private String statusEscrow;

    protected ContaEscrowEntity() {}

    public static ContaEscrowEntity fromDomain(ContaEscrow domain) {
        ContaEscrowEntity c = new ContaEscrowEntity();

        c.id = domain.getId();
        c.transacaoId = domain.getTransacaoId();
        c.prestadorId = domain.getPrestadorId();
        c.saldoRetido = domain.getSaldoRetido();
        c.statusEscrow = domain.getStatusEscrow().name();

        return c;
    }

    public ContaEscrow toDomain() {
        ContaEscrow escrow = new ContaEscrow(
                this.id,
                this.transacaoId,
                this.prestadorId,
                this.saldoRetido
        );

        escrow.reconstruirEstado(StatusEscrow.valueOf(this.statusEscrow));

        return escrow;
    }

}
