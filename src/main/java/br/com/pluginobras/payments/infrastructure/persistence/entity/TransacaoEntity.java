package br.com.pluginobras.payments.infrastructure.persistence.entity;

import br.com.pluginobras.payments.domain.model.transacao.StatusTransacao;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transacao_servico")
public class TransacaoEntity {

    @Id
    private UUID id;

    private String transacaoId;
    private String clienteId;
    private String prestadorId;

    private BigDecimal valorBase;
    private BigDecimal taxaCliente;
    private BigDecimal taxaPrestador;

    private BigDecimal valorPagoCliente;
    private BigDecimal valorLiquidoPrestador;
    private BigDecimal receitaPlataforma;

    @Column(name = "status")
    private String statusTransacao;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataLiberacao;

    protected TransacaoEntity() {}

    public static TransacaoEntity fromDomain(TransacaoServico domain) {
        TransacaoEntity e = new TransacaoEntity();

        e.id = domain.getId();
        e.transacaoId = domain.getTransacaoId();
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
                this.transacaoId,
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

}
