package br.com.pluginobras.payments.domain.model.transacao;

import br.com.pluginobras.payments.domain.exception.RegraNegocioException;
import br.com.pluginobras.payments.domain.model.disputa.DisputaManual;
import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.liberacao.LiberacaoPrestador;
import br.com.pluginobras.payments.domain.model.pagamento.Pagamento;
import br.com.pluginobras.payments.domain.model.pagamento.StatusPagamento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransacaoServico {

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

    private StatusTransacao statusTransacao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataLiberacao;

    protected TransacaoServico() {}

    public TransacaoServico(
            String transacaoId,
            String clienteId,
            String prestadorId,
            BigDecimal valorBase,
            BigDecimal taxaCliente,
            BigDecimal taxaPrestador
    ) {
        if (transacaoId == null || transacaoId.isBlank()) {
            throw new RegraNegocioException("Transação deve possuir um identificador válido.");
        }

        if (clienteId == null || clienteId.isBlank()) {
            throw new RegraNegocioException("Cliente deve ser informado.");
        }

        if (prestadorId == null || prestadorId.isBlank()) {
            throw new RegraNegocioException("Prestador deve ser informado.");
        }

        if (valorBase == null || valorBase.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Valor base deve ser maior que zero.");
        }

        if (taxaCliente == null || taxaCliente.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Taxa do cliente não pode ser negativa.");
        }

        if (taxaPrestador == null || taxaPrestador.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Taxa do prestador não pode ser negativa.");
        }

        this.id = UUID.randomUUID();
        this.transacaoId = transacaoId;
        this.clienteId = clienteId;
        this.prestadorId = prestadorId;
        this.valorBase = valorBase;
        this.taxaCliente = taxaCliente;
        this.taxaPrestador = taxaPrestador;

        calcularValores();

        this.statusTransacao = StatusTransacao.CRIADA;
        this.dataCriacao = LocalDateTime.now();
    }

    public void iniciarPagamento() {
        validarStatus(StatusTransacao.CRIADA);
        this.statusTransacao = StatusTransacao.AGUARDANDO_PAGAMENTO;
    }

    public void registrarPagamentoConfirmado() {
        validarStatus(StatusTransacao.AGUARDANDO_PAGAMENTO);
        this.statusTransacao = StatusTransacao.PAGA;
        this.dataPagamento = LocalDateTime.now();
    }

    public void criarEscrow() {
        validarStatus(StatusTransacao.PAGA);
        this.statusTransacao = StatusTransacao.EM_ESCROW;
    }

    public void registrarPagamentoConfirmado(Pagamento pagamento) {
        validarStatus(StatusTransacao.AGUARDANDO_PAGAMENTO);

        if(pagamento.getStatusPagamento() != StatusPagamento.CONFIRMADO) {
            throw new RegraNegocioException("Pagamento não está confirmado.");
        }

        this.statusTransacao = StatusTransacao.PAGA;
        this.dataPagamento = LocalDateTime.now();
    }

    public ContaEscrow contaEscrow() {
        validarStatus(StatusTransacao.PAGA);

        this.statusTransacao = StatusTransacao.EM_ESCROW;

        return new ContaEscrow(
                this.transacaoId,
                this.prestadorId,
                this.valorLiquidoPrestador
        );
    }

    public LiberacaoPrestador liberarPagamento(ContaEscrow contaEscrow) {
        validarStatus(StatusTransacao.EM_ESCROW);

        BigDecimal valor = contaEscrow.getSaldoRetido();
        contaEscrow.debitar(valor);
        contaEscrow.liberar();

        this.statusTransacao = StatusTransacao.LIBERADA;
        this.dataLiberacao = LocalDateTime.now();

        return new LiberacaoPrestador(
                this.transacaoId,
                this.prestadorId,
                valor
        );
    }

    public DisputaManual abrirDisputa(BigDecimal valorDevolvidoCliente, String motivo) {
        validarStatus(StatusTransacao.EM_ESCROW);

        this.statusTransacao = StatusTransacao.EM_DISPUTA;

        return new DisputaManual(
                this.transacaoId,
                valorDevolvidoCliente,
                motivo
        );
    }

    public void resolverDisputa(ContaEscrow contaEscrow, DisputaManual disputaManual) {
        validarStatus(StatusTransacao.EM_DISPUTA);

        BigDecimal devolucao = disputaManual.getValorDevolvidoCliente();

        if(devolucao.compareTo(BigDecimal.ZERO) > 0) {
            contaEscrow.debitar(devolucao);
        }

        disputaManual.resolver();

        if(contaEscrow.getSaldoRetido().compareTo(BigDecimal.ZERO) == 0) {
            this.statusTransacao = StatusTransacao.CONCLUIDA;
        } else {
            this.statusTransacao = StatusTransacao.EM_ESCROW;
        }
    }

    private void calcularValores() {
        this.valorPagoCliente = valorBase.add(taxaCliente);
        this.valorLiquidoPrestador = valorBase.subtract(taxaPrestador);
        this.receitaPlataforma = taxaCliente.add(taxaPrestador);
    }

    private void validarStatus(StatusTransacao statusTransacao) {
        if(this.statusTransacao != statusTransacao) {
            throw new RegraNegocioException("Transação em estado inválido. Esperado: " + statusTransacao + ", atual: " + this.statusTransacao);
        }
    }

}
