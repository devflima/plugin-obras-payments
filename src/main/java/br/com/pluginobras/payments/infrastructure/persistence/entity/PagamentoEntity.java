package br.com.pluginobras.payments.infrastructure.persistence.entity;

import br.com.pluginobras.payments.domain.model.pagamento.Pagamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagamento")
public class PagamentoEntity {

    @Id
    private UUID id;

    private String transacaoId;
    private String meioPagamento;
    private BigDecimal valorBruto;
    private BigDecimal taxaGateway;
    private BigDecimal valorLiquido;

    private String statusPagamento;

    private LocalDateTime dataEvento;

    protected PagamentoEntity() {}

    public static PagamentoEntity fromDomain(Pagamento domain) {
        PagamentoEntity p = new PagamentoEntity();

        p.id = domain.getId();
        p.transacaoId = domain.getTransacaoId();
        p.meioPagamento = domain.getMeioPagamento().name();
        p.valorBruto = domain.getValorBruto();
        p.taxaGateway = domain.getTaxaGateway();
        p.valorLiquido = domain.getValorLiquido();
        p.statusPagamento = domain.getStatusPagamento().name();
        p.dataEvento = domain.getDataEvento();

        return p;
    }

}
