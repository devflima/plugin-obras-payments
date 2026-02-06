package br.com.pluginobras.payments.support.fake;

import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;

import java.math.BigDecimal;
import java.util.UUID;

public class ContaEscrowFake {

    public static ContaEscrow ativa() {
        return ContaEscrow.criar(
                UUID.randomUUID(),
                "P1",
                new BigDecimal("95")
        );
    }

    public static ContaEscrow comSaldo(BigDecimal valor) {
        return ContaEscrow.criar(
                UUID.randomUUID(),
                "P1",
                valor
        );
    }
}