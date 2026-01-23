package br.com.pluginobras.payments.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RelatorioEscrowPrestadorResponse {

    private String prestadorId;

    private BigDecimal totalRetido;
    private BigDecimal totalLiberado;
    private BigDecimal totalEmDisputa;

}
