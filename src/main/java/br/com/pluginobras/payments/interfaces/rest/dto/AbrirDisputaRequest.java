package br.com.pluginobras.payments.interfaces.rest.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AbrirDisputaRequest {

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal valorDevolvidoCliente;

    @NotBlank
    private String motivo;

}
