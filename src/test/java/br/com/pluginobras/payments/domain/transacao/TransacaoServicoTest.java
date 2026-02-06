package br.com.pluginobras.payments.domain.transacao;

import br.com.pluginobras.payments.domain.exception.RegraNegocioException;
import br.com.pluginobras.payments.domain.model.disputa.DisputaManual;
import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.transacao.StatusTransacao;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.support.fake.DisputaFake;
import br.com.pluginobras.payments.support.fake.PagamentoFake;
import br.com.pluginobras.payments.support.fake.TransacaoFake;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
public class TransacaoServicoTest {

    @Test
    void deveCalcularValoresCorretamente() {
        TransacaoServico t = TransacaoFake.criada();

        assertThat(t.getValorPagoCliente()).isEqualByComparingTo("110");
        assertThat(t.getValorLiquidoPrestador()).isEqualByComparingTo("95");
        assertThat(t.getReceitaPlataforma()).isEqualByComparingTo("15");
    }

    @Test
    void deveRegistrarPagamentoEIrParaEscrow() {
        TransacaoServico t = TransacaoFake.criada();

        ContaEscrow escrow = t.registrarPagamento(
                PagamentoFake.pendente(t.getId())
        );

        assertThat(t.getStatusTransacao())
                .isEqualTo(StatusTransacao.EM_ESCROW);

        assertThat(t.getValorPagoCliente()).isEqualByComparingTo("110");
        assertThat(t.getValorLiquidoPrestador()).isEqualByComparingTo("95");
        assertThat(t.getReceitaPlataforma()).isEqualByComparingTo("15");
    }


    @Test
    void naoDeveRegistrarPagamentoSeNaoEstiverCriada() {
        TransacaoServico t = TransacaoFake.emEscrow();

        assertThatThrownBy(() ->
                t.registrarPagamento(PagamentoFake.confirmado(t.getId()))
        ).isInstanceOf(RegraNegocioException.class);
    }

    @Test
    void deveAbrirDisputa() {
        TransacaoServico t = TransacaoFake.emEscrow();

        DisputaManual disputa = t.abrirDisputa(
                new BigDecimal("20"), "Problema"
        );

        assertThat(t.getStatusTransacao()).isEqualTo(StatusTransacao.EM_DISPUTA);
        assertThat(disputa).isNotNull();
    }

    @Test
    void deveResolverDisputaParcialEVoltarParaEscrow() {
        TransacaoServico t = TransacaoFake.emDisputa();
        ContaEscrow escrow = new ContaEscrow(
                t.getId(), "P1", new BigDecimal("95")
        );

        DisputaManual disputa = DisputaFake.parcial(t.getId());

        t.resolverDisputa(escrow, disputa);

        assertThat(t.getStatusTransacao()).isEqualTo(StatusTransacao.EM_ESCROW);
        assertThat(escrow.getSaldoRetido()).isEqualByComparingTo("75");
    }

    @Test
    void deveResolverDisputaTotalELiberar() {
        TransacaoServico t = TransacaoFake.emDisputa();
        ContaEscrow escrow = new ContaEscrow(
                t.getId(), "P1", new BigDecimal("95")
        );

        DisputaManual disputa = DisputaFake.total(t.getId());

        t.resolverDisputa(escrow, disputa);

        assertThat(t.getStatusTransacao()).isEqualTo(StatusTransacao.LIBERADA);
        assertThat(escrow.getSaldoRetido()).isEqualByComparingTo("0");
    }
}
