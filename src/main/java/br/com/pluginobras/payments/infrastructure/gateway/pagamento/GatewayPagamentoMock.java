package br.com.pluginobras.payments.infrastructure.gateway.pagamento;

import br.com.pluginobras.payments.domain.model.transacao.MeioPagamento;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GatewayPagamentoMock implements GatewayPagamentoClient {

    @Override
    public ResultadoPagamento processar(MeioPagamento meioPagamento, BigDecimal valor) {
        return switch (meioPagamento) {
            case PIX -> new ResultadoPagamento(true, valor, BigDecimal.ZERO);
            case CARTAO ->  new ResultadoPagamento(true, valor, valor.multiply(new BigDecimal("0.03")));
            case BOLETO -> new ResultadoPagamento(true, valor, new BigDecimal("2.50"));
        };
    }

}
