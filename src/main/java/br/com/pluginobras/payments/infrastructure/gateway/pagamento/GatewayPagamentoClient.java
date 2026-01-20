package br.com.pluginobras.payments.infrastructure.gateway.pagamento;

import br.com.pluginobras.payments.domain.model.transacao.MeioPagamento;

import java.math.BigDecimal;

public interface GatewayPagamentoClient {

    ResultadoPagamento processar(
            MeioPagamento meioPagamento,
            BigDecimal valor
    );

}
