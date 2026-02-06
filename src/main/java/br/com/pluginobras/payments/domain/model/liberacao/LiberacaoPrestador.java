package br.com.pluginobras.payments.domain.model.liberacao;

import br.com.pluginobras.payments.domain.exception.RegraNegocioException;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LiberacaoPrestador {

    private UUID transacaoId;
    private String prestadorId;
    private BigDecimal valorLiberado;
    private LocalDateTime dataLiberacao;

    protected LiberacaoPrestador() {}

    public LiberacaoPrestador(
            UUID transacaoId,
            String prestadorId,
            BigDecimal valorLiberado
    ) {
        if(valorLiberado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Valor liberado deve ser maior que zero.");
        }

        this.transacaoId = transacaoId;
        this.prestadorId = prestadorId;
        this.valorLiberado = valorLiberado;
        this.dataLiberacao = LocalDateTime.now();
    }

}
