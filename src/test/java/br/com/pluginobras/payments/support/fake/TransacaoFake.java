package br.com.pluginobras.payments.support.fake;

import br.com.pluginobras.payments.domain.model.transacao.StatusTransacao;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransacaoFake {

    public static TransacaoServico criada() {
        return new TransacaoServico(
                "C1",
                "P1",
                new BigDecimal("100.00"),
                new BigDecimal("10.00"),
                new BigDecimal("5.00")
        );
    }

    public static TransacaoServico emEscrow() {
        TransacaoServico t = criada();
        t.reconstruirEstado(
                StatusTransacao.EM_ESCROW,
                LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now().minusMinutes(5),
                null
        );
        return t;
    }

    public static TransacaoServico emDisputa() {
        TransacaoServico t = criada();
        t.reconstruirEstado(
                StatusTransacao.EM_DISPUTA,
                LocalDateTime.now().minusMinutes(20),
                LocalDateTime.now().minusMinutes(15),
                null
        );
        return t;
    }

    public static TransacaoServico liberada() {
        TransacaoServico t = criada();
        t.reconstruirEstado(
                StatusTransacao.LIBERADA,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().minusMinutes(50),
                LocalDateTime.now().minusMinutes(10)
        );
        return t;
    }
}
