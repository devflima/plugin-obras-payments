package br.com.pluginobras.payments.infrastructure.persistence.repository.projection;

import java.math.BigDecimal;

public interface TotalRetidoPorPrestador {
    String getPrestadorId();
    BigDecimal getTotalRetido();
}
