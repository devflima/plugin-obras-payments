package br.com.pluginobras.payments.domain.model.pagamento;

import br.com.pluginobras.payments.domain.exception.RegraNegocioException;
import br.com.pluginobras.payments.domain.model.transacao.MeioPagamento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Pagamento {

    private UUID transacaoId;

    private MeioPagamento meioPagamento;
    private BigDecimal valorBruto;
    private BigDecimal taxaGateway;
    private BigDecimal valorLiquido;

    private StatusPagamento statusPagamento;
    private LocalDateTime dataEvento;

    protected Pagamento() {}

    public Pagamento(
            UUID transacaoId,
            MeioPagamento meioPagamento,
            BigDecimal valorBruto,
            BigDecimal taxaGateway
    ) {
        this.transacaoId = transacaoId;
        this.meioPagamento = meioPagamento;
        this.valorBruto = valorBruto;
        this.taxaGateway = taxaGateway;
        this.valorLiquido = valorBruto.subtract(taxaGateway);
        this.statusPagamento = StatusPagamento.PENDENTE;
        this.dataEvento = LocalDateTime.now();
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void confirmar() {
        if(this.statusPagamento != StatusPagamento.PENDENTE) {
            throw new RegraNegocioException("Pagamento não pode ser confirmado neste estado: " + statusPagamento);
        }

        this.statusPagamento = StatusPagamento.CONFIRMADO;
    }

    public void falhar() {
        if(this.statusPagamento != StatusPagamento.PENDENTE) {
            throw new RegraNegocioException("Pagamento não pode falhar neste estado: " + statusPagamento);
        }
        this.statusPagamento = StatusPagamento.FALHOU;
    }

}
