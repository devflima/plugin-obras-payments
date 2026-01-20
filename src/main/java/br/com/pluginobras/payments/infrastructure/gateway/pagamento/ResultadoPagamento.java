package br.com.pluginobras.payments.infrastructure.gateway.pagamento;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResultadoPagamento {

    private final boolean aprovado;
    private final BigDecimal valorBruto;
    private final BigDecimal taxaGateway;

    public ResultadoPagamento(boolean aprovado, BigDecimal valorBruto, BigDecimal taxaGateway) {
        this.aprovado = aprovado;
        this.valorBruto = valorBruto;
        this.taxaGateway = taxaGateway;
    }

    public BigDecimal valorLiquido() {
        return valorBruto.subtract(taxaGateway);
    }

}
