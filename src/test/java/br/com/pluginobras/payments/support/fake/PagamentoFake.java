package br.com.pluginobras.payments.support.fake;

import br.com.pluginobras.payments.domain.model.pagamento.Pagamento;
import br.com.pluginobras.payments.domain.model.transacao.MeioPagamento;

import java.math.BigDecimal;
import java.util.UUID;

public class PagamentoFake {

    public static Pagamento pendente(UUID transacaoId) {
        Pagamento p = new Pagamento(
                transacaoId,
                MeioPagamento.PIX,
                new BigDecimal("110.00"),
                new BigDecimal("5.00")
        );

        return p;
    }

    public static Pagamento confirmado(UUID transacaoId) {
        Pagamento p = pendente(transacaoId);
        p.confirmar();
        return p;
    }
}
