package br.com.pluginobras.payments.interfaces.rest.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CriarTransacaoResponse {

    private String transacaoId;
    private String status;
    private BigDecimal valorPagoCliente;

    public CriarTransacaoResponse(String transacaoId, String status, BigDecimal valorPagoCliente) {
        this.transacaoId = transacaoId;
        this.status = status;
        this.valorPagoCliente = valorPagoCliente;
    }

}

