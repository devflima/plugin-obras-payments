package br.com.pluginobras.payments.infrastructure.persistence.entity;

import br.com.pluginobras.payments.domain.model.transacao.StatusTransacao;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacao_servico")
@Data
public class TransacaoEntity {

    @Id
    private UUID id;
    private String clienteId;
    private String prestadorId;

    private BigDecimal valorBase;
    private BigDecimal taxaCliente;
    private BigDecimal taxaPrestador;

    private BigDecimal valorPagoCliente;
    private BigDecimal valorLiquidoPrestador;
    private BigDecimal receitaPlataforma;

    private String statusTransacao;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataLiberacao;

    protected TransacaoEntity() {}

    public static TransacaoEntity fromDomain(TransacaoServico domain) {
        TransacaoEntity e = new TransacaoEntity();

        e.id = domain.getId();
        e.clienteId = domain.getClienteId();
        e.prestadorId = domain.getPrestadorId();
        e.valorBase = domain.getValorBase();
        e.taxaCliente = domain.getTaxaCliente();
        e.taxaPrestador = domain.getTaxaPrestador();
        e.valorPagoCliente = domain.getValorPagoCliente();
        e.valorLiquidoPrestador = domain.getValorLiquidoPrestador();
        e.receitaPlataforma = domain.getReceitaPlataforma();
        e.statusTransacao = domain.getStatusTransacao().name();
        e.dataCriacao = domain.getDataCriacao();
        e.dataPagamento = domain.getDataPagamento();
        e.dataLiberacao = domain.getDataLiberacao();

        return e;
    }

    public TransacaoServico toDomain() {
        TransacaoServico t = new TransacaoServico(
                this.id,
                this.clienteId,
                this.prestadorId,
                this.valorBase,
                this.taxaCliente,
                this.taxaPrestador
        );

        t.reconstruirEstado(
                StatusTransacao.valueOf(this.statusTransacao),
                this.dataCriacao,
                this.dataPagamento,
                this.dataLiberacao
        );

        return t;
    }

    public void updateToDomain(TransacaoServico domain) {
        this.statusTransacao = StatusTransacao.EM_ESCROW.name();
        this.dataPagamento = LocalDateTime.now();
        this.valorPagoCliente = domain.getValorPagoCliente();
        this.valorLiquidoPrestador = domain.getValorLiquidoPrestador();
        this.receitaPlataforma = domain.getReceitaPlataforma();
    }

    public void applyStateFromDomain(TransacaoServico domain) {
        this.statusTransacao = domain.getStatusTransacao().name();
        this.valorBase = domain.getValorBase();
        this.valorPagoCliente = domain.getValorPagoCliente();
        this.valorLiquidoPrestador = domain.getValorLiquidoPrestador();
        this.receitaPlataforma = domain.getReceitaPlataforma();
        this.dataLiberacao = domain.getDataLiberacao();
    }

}
