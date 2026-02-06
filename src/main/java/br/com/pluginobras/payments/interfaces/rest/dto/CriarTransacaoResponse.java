package br.com.pluginobras.payments.interfaces.rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CriarTransacaoResponse {

    private UUID transacaoId;
    private String status;
    private BigDecimal valorPagoCliente;

    public CriarTransacaoResponse(UUID transacaoId, String status, BigDecimal valorPagoCliente) {
        this.transacaoId = transacaoId;
        this.status = status;
        this.valorPagoCliente = valorPagoCliente;
    }

}

