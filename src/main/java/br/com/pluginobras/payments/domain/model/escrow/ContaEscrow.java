package br.com.pluginobras.payments.domain.model.escrow;

import br.com.pluginobras.payments.domain.exception.RegraNegocioException;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ContaEscrow {

    private UUID id;
    private String transacaoId;
    private String prestadorId;

    private BigDecimal saldoRetido;
    private StatusEscrow statusEscrow;

    public ContaEscrow(
            UUID id,
            String transacaoId,
            String prestadorId,
            BigDecimal valorLiquido
    ) {
        this.id = id;
        this.transacaoId = transacaoId;
        this.prestadorId = prestadorId;
        this.saldoRetido = valorLiquido;
    }

    public ContaEscrow(
            String transacaoId,
            String prestadorId,
            BigDecimal valorLiquido
    ) {
        if(valorLiquido.compareTo(BigDecimal.ZERO) <=0) {
            throw new RegraNegocioException("Valor inicial do escrow deve ser maior que zero.");
        }

        this.id = UUID.randomUUID();
        this.transacaoId = transacaoId;
        this.prestadorId = prestadorId;
        this.saldoRetido = valorLiquido;
        this.statusEscrow = StatusEscrow.ATIVO;
    }

    public static ContaEscrow criar(String transacaoId, String prestadorId, BigDecimal valorLiquido) {
        return new ContaEscrow(transacaoId, prestadorId, valorLiquido);
    }

    public void reconstruirEstado(StatusEscrow statusEscrow) {
        this.statusEscrow = statusEscrow;
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
