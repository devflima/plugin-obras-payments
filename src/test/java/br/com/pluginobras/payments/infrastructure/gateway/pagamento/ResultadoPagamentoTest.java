package br.com.pluginobras.payments.infrastructure.gateway.pagamento;

import br.com.pluginobras.payments.domain.model.transacao.MeioPagamento;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultadoPagamentoTest {

    @Test
    void deve_simular_pix_sem_taxa() {
        GatewayPagamentoClient g = new GatewayPagamentoMock();
        ResultadoPagamento r = g.processar(MeioPagamento.PIX, new BigDecimal("100"));

        assertTrue(r.isAprovado());
        assertEquals(new BigDecimal("100"), r.valorLiquido());
    }

}
