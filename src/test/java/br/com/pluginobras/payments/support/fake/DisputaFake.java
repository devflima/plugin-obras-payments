package br.com.pluginobras.payments.support.fake;

import br.com.pluginobras.payments.domain.model.disputa.DisputaManual;

import java.math.BigDecimal;
import java.util.UUID;

public class DisputaFake {
    public static DisputaManual parcial(UUID transacaoId) {
        return new DisputaManual(
                transacaoId,
                new BigDecimal("20"),
                "Serviço incompleto"
        );
    }

    public static DisputaManual total(UUID transacaoId) {
        return new DisputaManual(
                transacaoId,
                new BigDecimal("95"),
                "Serviço não realizado"
        );
    }
}
