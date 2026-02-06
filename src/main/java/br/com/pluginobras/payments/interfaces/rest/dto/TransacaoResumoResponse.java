package br.com.pluginobras.payments.interfaces.rest.dto;

import br.com.pluginobras.payments.domain.model.transacao.StatusTransacao;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoResumoResponse {

    private UUID transacaoId;
    private String clienteId;
    private String prestadorId;
    private StatusTransacao statusTransacao;
    private BigDecimal valorPagoCliente;
    private BigDecimal valorLiquidoPrestador;
    private LocalDateTime dataCriacao;

    public static TransacaoResumoResponse fromEntity(TransacaoEntity entity) {
        TransacaoResumoResponse dto = new TransacaoResumoResponse();

        dto.transacaoId = entity.getId();
        dto.clienteId = entity.getClienteId();
        dto.prestadorId = entity.getPrestadorId();
        dto.statusTransacao = StatusTransacao.valueOf(entity.getStatusTransacao());
        dto.valorPagoCliente = entity.getValorPagoCliente();
        dto.valorLiquidoPrestador = entity.getValorLiquidoPrestador();
        dto.dataCriacao = entity.getDataCriacao();

        return dto;
    }

}
