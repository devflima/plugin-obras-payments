package br.com.pluginobras.payments.domain.escrow;

import br.com.pluginobras.payments.domain.exception.RegraNegocioException;
import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.escrow.StatusEscrow;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ContaEscrowTest {

    @Test
    void deveCriarEscrowComSaldoInicial() {
        ContaEscrow escrow = ContaEscrow.criar(
                UUID.randomUUID(), "P1", new BigDecimal("100")
        );

        assertThat(escrow.getSaldoRetido()).isEqualByComparingTo("100");
        assertThat(escrow.getStatusEscrow()).isEqualTo(StatusEscrow.ATIVO);
    }

    @Test
    void naoDeveCriarEscrowComValorZeroOuNegativo() {
        assertThatThrownBy(() ->
                ContaEscrow.criar(UUID.randomUUID(), "P1", BigDecimal.ZERO)
        ).isInstanceOf(RegraNegocioException.class);
    }

    @Test
    void deveDebitarSaldo() {
        ContaEscrow escrow = ContaEscrow.criar(
                UUID.randomUUID(), "P1", new BigDecimal("100")
        );

        escrow.debitar(new BigDecimal("40"));

        assertThat(escrow.getSaldoRetido()).isEqualByComparingTo("60");
    }

    @Test
    void naoDeveDebitarMaisQueSaldo() {
        ContaEscrow escrow = ContaEscrow.criar(
                UUID.randomUUID(), "P1", new BigDecimal("50")
        );

        assertThatThrownBy(() ->
                escrow.debitar(new BigDecimal("60"))
        ).isInstanceOf(RegraNegocioException.class);
    }

    @Test
    void deveLiberarQuandoSaldoZerado() {
        ContaEscrow escrow = ContaEscrow.criar(
                UUID.randomUUID(), "P1", new BigDecimal("50")
        );

        escrow.debitar(new BigDecimal("50"));
        escrow.liberar();

        assertThat(escrow.getStatusEscrow()).isEqualTo(StatusEscrow.LIBERADO);
    }

    @Test
    void naoDeveLiberarComSaldo() {
        ContaEscrow escrow = ContaEscrow.criar(
                UUID.randomUUID(), "P1", new BigDecimal("50")
        );

        assertThatThrownBy(escrow::liberar)
                .isInstanceOf(RegraNegocioException.class);
    }
}
