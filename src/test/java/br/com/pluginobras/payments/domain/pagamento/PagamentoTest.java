package br.com.pluginobras.payments.domain.pagamento;

import br.com.pluginobras.payments.domain.exception.RegraNegocioException;
import br.com.pluginobras.payments.domain.model.pagamento.Pagamento;
import br.com.pluginobras.payments.domain.model.pagamento.StatusPagamento;
import br.com.pluginobras.payments.domain.model.transacao.MeioPagamento;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PagamentoTest {

    @Test
    void deveCriarPagamentoEmEstadoPendente() {
        Pagamento pagamento = new Pagamento(
                UUID.randomUUID(),
                MeioPagamento.PIX,
                new BigDecimal("110"),
                new BigDecimal("10")
        );

        assertThat(pagamento.getStatusPagamento()).isEqualTo(StatusPagamento.PENDENTE);
    }

    @Test
    void deveConfirmarPagamentoQuandoPendente() {
        Pagamento pagamento = new Pagamento(
                UUID.randomUUID(),
                MeioPagamento.PIX,
                new BigDecimal("110"),
                new BigDecimal("10")
        );

        pagamento.confirmar();

        assertThat(pagamento.getStatusPagamento()).isEqualTo(StatusPagamento.CONFIRMADO);
        assertThat(pagamento.getDataEvento()).isNotNull();
    }

    @Test
    void naoDevePermitirConfirmarPagamentoJaConfirmado() {
        Pagamento pagamento = new Pagamento(
                UUID.randomUUID(),
                MeioPagamento.PIX,
                new BigDecimal("110"),
                new BigDecimal("10")
        );

        pagamento.confirmar();

        assertThatThrownBy(pagamento::confirmar)
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("Pagamento não pode ser confirmado neste estado");
    }

    @Test
    void deveManterMesmoIdAoConfirmarPagamento() {
        UUID pagamentoId = UUID.randomUUID();

        Pagamento pagamento = new Pagamento(
                pagamentoId,
                MeioPagamento.PIX,
                new BigDecimal("110"),
                new BigDecimal("10")
        );

        pagamento.confirmar();

        assertThat(pagamento.getTransacaoId()).isEqualTo(pagamentoId);
    }
}

