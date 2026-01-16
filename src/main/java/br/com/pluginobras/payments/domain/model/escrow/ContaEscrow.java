package br.com.pluginobras.payments.domain.model.escrow;

import br.com.pluginobras.payments.domain.exception.RegraNegocioException;

import java.math.BigDecimal;
import java.util.UUID;

public class ContaEscrow {

    private UUID id;
    private String transacaoId;
    private String prestadorId;

    private BigDecimal saldoRetido;
    private StatusEscrow statusEscrow;

    protected ContaEscrow() {}

    public ContaEscrow(
            String transacaoId,
            String prestadorId,
            BigDecimal valorInicial
    ) {
        if(valorInicial.compareTo(BigDecimal.ZERO) <=0) {
            throw new RegraNegocioException("Valor inicial do escrow deve ser maior que zero.");
        }

        this.id = UUID.randomUUID();
        this.transacaoId = transacaoId;
        this.prestadorId = prestadorId;
        this.saldoRetido = valorInicial;
        this.statusEscrow = StatusEscrow.ATIVO;
    }

    public BigDecimal getSaldoRetido() {
        return saldoRetido;
    }

    public void debitar(BigDecimal valor) {
        validarAtivo();

        if(valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Valor para débito deve ser maior que zero.");
        }

        if(saldoRetido.compareTo(valor) < 0) {
            throw new RegraNegocioException("Saldo insuficiente no escrow.");
        }

        this.saldoRetido = this.saldoRetido.subtract(valor);
    }

    public void bloquear() {
        validarAtivo();
        this.statusEscrow = StatusEscrow.BLOQUEADO;
    }

    public void liberar() {
        validarAtivo();

        if(saldoRetido.compareTo(BigDecimal.ZERO) > 0) {
            throw new RegraNegocioException("Não é possível liberar escrow com saldo pendente.");
        }

        this.statusEscrow = StatusEscrow.LIBERADO;
    }

    private void validarAtivo() {
        if(this.statusEscrow != StatusEscrow.ATIVO) {
            throw new RegraNegocioException("Conta escrow não está ativa. Status atual: " + statusEscrow);
        }
    }

}
