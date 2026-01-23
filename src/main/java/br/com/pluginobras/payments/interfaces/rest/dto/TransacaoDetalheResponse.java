package br.com.pluginobras.payments.interfaces.rest.dto;

import br.com.pluginobras.payments.domain.model.transacao.StatusTransacao;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoDetalheResponse {

    private String transacaoId;
    private String clienteId;
    private String prestadorId;

    private BigDecimal valorBase;
    private BigDecimal taxaCliente;
    private BigDecimal taxaPrestador;
    private BigDecimal valorPagoCliente;
    private BigDecimal valorLiquidoPrestador;
    private BigDecimal receitaPlataforma;

    private StatusTransacao status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataLiberacao;

    public static TransacaoDetalheResponse fromEntity(TransacaoEntity entity) {
        TransacaoDetalheResponse dto = new TransacaoDetalheResponse();
        dto.transacaoId = entity.getTransacaoId();
        dto.clienteId = entity.getClienteId();
        dto.prestadorId = entity.getPrestadorId();

        dto.valorBase = entity.getValorBase();
        dto.taxaCliente = entity.getTaxaCliente();
        dto.taxaPrestador = entity.getTaxaPrestador();
        dto.valorPagoCliente = entity.getValorPagoCliente();
        dto.valorLiquidoPrestador = entity.getValorLiquidoPrestador();
        dto.receitaPlataforma = entity.getReceitaPlataforma();

        dto.status = StatusTransacao.valueOf(entity.getStatusTransacao());
        dto.dataCriacao = entity.getDataCriacao();
        dto.dataPagamento = entity.getDataPagamento();
        dto.dataLiberacao = entity.getDataLiberacao();

        return dto;
    }
}

