package br.com.pluginobras.payments.interfaces.rest.dto;

import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TransacaoCsvDTO {

    private UUID transacaoId;
    private String clienteId;
    private String prestadorId;
    private String statusTranscao;
    private BigDecimal valorBase;
    private BigDecimal valorPagoCliente;
    private BigDecimal valorLiquidoPrestador;
    private BigDecimal receitaPlataforma;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataLiberacao;

    public static TransacaoCsvDTO fromEntity(TransacaoEntity entity) {
        return new TransacaoCsvDTO(
                entity.getId(),
                entity.getClienteId(),
                entity.getPrestadorId(),
                entity.getStatusTransacao(),
                entity.getValorBase(),
                entity.getValorPagoCliente(),
                entity.getValorLiquidoPrestador(),
                entity.getReceitaPlataforma(),
                entity.getDataCriacao(),
                entity.getDataPagamento(),
                entity.getDataLiberacao()
        );
    }

    public String toCsvLine() {
        return String.join(";",
                transacaoId.toString(),
                clienteId,
                prestadorId,
                statusTranscao,
                valorBase.toString(),
                valorPagoCliente.toString(),
                valorLiquidoPrestador.toString(),
                receitaPlataforma.toString(),
                valueOrEmpty(dataCriacao),
                valueOrEmpty(dataPagamento),
                valueOrEmpty(dataLiberacao)
        );
    }

    public static String valueOrEmpty(LocalDateTime value) {
        return value != null ? value.toString() : "";
    }

}
