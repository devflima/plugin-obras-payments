package br.com.pluginobras.payments.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriarTransacaoRequest {

    @NotBlank
    private String clienteId;

    @NotBlank
    private String prestadorId;

    @NotNull
    @Positive
    private BigDecimal valorBase;

    @NotNull @PositiveOrZero
    private BigDecimal taxaCliente;

    @NotNull @PositiveOrZero
    private BigDecimal taxaPrestador;
}
