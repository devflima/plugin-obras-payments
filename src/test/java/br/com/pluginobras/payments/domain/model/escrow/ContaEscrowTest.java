package br.com.pluginobras.payments.domain.model.escrow;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContaEscrowTest {

    @Test
    void deveCriarEscrowComSaldoInicial() {
        ContaEscrow escrow = ContaEscrow.criar("T1", "P1", new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), escrow.getSaldoRetido());
    }

    @Test
    void deveZerarSaldoAoLiberar() {
        ContaEscrow escrow = ContaEscrow.criar("T1", "P1", new BigDecimal("100.00"));
        escrow.debitar(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("0.00"), escrow.getSaldoRetido());
    }

    @Test
    void deveManterSaldoAposDisputaParcial() {
        ContaEscrow escrow = ContaEscrow.criar("T1", "P1", new BigDecimal("100.00"));
        escrow.debitar(new BigDecimal("40.00"));

        assertEquals(new BigDecimal("60.00"), escrow.getSaldoRetido());
    }

    @Test
    void deveZerarSaldoEmDisputaTotal() {
        ContaEscrow escrow = ContaEscrow.criar("T1", "P1", new BigDecimal("100.00"));
        escrow.debitar(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("0.00"), escrow.getSaldoRetido());
    }

}
