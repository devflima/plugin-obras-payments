package br.com.pluginobras.payments.domain.model.transacao;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransacaoServicoTest {

    @Test
    void deveCalcularValoresCorretamente() {
        TransacaoServico t = new TransacaoServico(
                "T-1",
                "C-1",
                "P-1",
                new BigDecimal("100.00"),
                new BigDecimal("10.00"),
                new BigDecimal("5.00")
        );

        assertEquals(new BigDecimal("110.00"), t.getValorPagoCliente());
        assertEquals(new BigDecimal("95.00"), t.getValorLiquidoPrestador());
        assertEquals(new BigDecimal("15.00"), t.getReceitaPlataforma());
    }

}
