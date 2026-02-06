package br.com.pluginobras.payments.infrastructure.gateway;

import br.com.pluginobras.payments.domain.model.transacao.MeioPagamento;
import br.com.pluginobras.payments.infrastructure.gateway.pagamento.GatewayPagamentoClient;
import br.com.pluginobras.payments.infrastructure.gateway.pagamento.GatewayPagamentoMock;
import br.com.pluginobras.payments.infrastructure.gateway.pagamento.ResultadoPagamento;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultadoPagamentoTest {

    @Test
    void deveSimularPixSemTaxa() {
        GatewayPagamentoClient g = new GatewayPagamentoMock();
        ResultadoPagamento r = g.processar(MeioPagamento.PIX, new BigDecimal("100"));

        assertTrue(r.isAprovado());
        assertEquals(new BigDecimal("100"), r.valorLiquido());
    }

}
