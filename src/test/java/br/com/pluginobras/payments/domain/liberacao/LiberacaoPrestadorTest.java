package br.com.pluginobras.payments.domain.liberacao;

import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.escrow.StatusEscrow;
import br.com.pluginobras.payments.domain.model.transacao.StatusTransacao;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.support.fake.ContaEscrowFake;
import br.com.pluginobras.payments.support.fake.TransacaoFake;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LiberacaoPrestadorTest {
    @Test
    void deveLiberarPagamentoQuandoEmEscrow() {

        TransacaoServico transacao = TransacaoFake.emEscrow();
        ContaEscrow escrow = ContaEscrowFake.comSaldo(transacao.getValorLiquidoPrestador());

        transacao.liberarPagamento(escrow);

        assertThat(transacao.getStatusTransacao())
                .isEqualTo(StatusTransacao.LIBERADA);

        assertThat(transacao.getDataLiberacao()).isNotNull();

        assertThat(escrow.getStatusEscrow())
                .isEqualTo(StatusEscrow.LIBERADO);
    }
}
