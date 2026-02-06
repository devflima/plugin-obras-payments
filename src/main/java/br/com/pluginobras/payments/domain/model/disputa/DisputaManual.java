package br.com.pluginobras.payments.domain.model.disputa;

import br.com.pluginobras.payments.domain.exception.RegraNegocioException;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DisputaManual {

    private UUID transacaoId;
    private BigDecimal valorDevolvidoCliente;
    private String motivo;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataResolucao;
    private StatusDisputa statusDisputa;

    public DisputaManual(
            UUID transacaoId,
            BigDecimal valorDevolvidoCliente,
            String motivo
    ) {
        if(valorDevolvidoCliente.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Valor devolvido não pode ser negativo.");
        }

        this.transacaoId = transacaoId;
        this.valorDevolvidoCliente = valorDevolvidoCliente;
        this.motivo = motivo;
        this.statusDisputa = StatusDisputa.ABERTA;
        this.dataAbertura = LocalDateTime.now();
    }

    public void resolver() {
        if(this.statusDisputa != StatusDisputa.ABERTA) {
            throw new RegraNegocioException("Disputa já está resolvida.");
        }

        this.statusDisputa = StatusDisputa.RESOLVIDA;
        this.dataResolucao = LocalDateTime.now();
    }

    public BigDecimal getValorDevolvidoCliente() {
        return valorDevolvidoCliente;
    }

    public void recontruirEstado(StatusDisputa statusDisputa, LocalDateTime dataAbertura, LocalDateTime dataResolucao) {
        this.statusDisputa = statusDisputa;
        this.dataAbertura = dataAbertura;
        this.dataResolucao = dataResolucao;
    }

}
