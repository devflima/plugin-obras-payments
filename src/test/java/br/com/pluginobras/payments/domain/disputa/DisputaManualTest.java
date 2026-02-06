package br.com.pluginobras.payments.domain.disputa;

import br.com.pluginobras.payments.domain.model.disputa.DisputaManual;
import br.com.pluginobras.payments.domain.model.disputa.StatusDisputa;
import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DisputaManualTest {
    @Test
    void deveResolverDisputaParcial() {

        TransacaoServico transacao = new TransacaoServico(
                UUID.randomUUID(),
                "C1",
                "P1",
                new BigDecimal("100"),
                new BigDecimal("10"),
                new BigDecimal("5")
        );

        ContaEscrow escrow = new ContaEscrow(
                transacao.getId(),
                transacao.getPrestadorId(),
                transacao.getValorLiquidoPrestador()
        );

        DisputaManual disputa = new DisputaManual(
                transacao.getId(),
                new BigDecimal("20"),
                "Problema"
        );

        // act
        transacao.resolverDisputa(escrow, disputa);

        // assert
        assertThat(disputa.getStatusDisputa()).isEqualTo(StatusDisputa.RESOLVIDA);
        assertThat(escrow.getSaldoRetido()).isEqualByComparingTo("75");
    }
}
