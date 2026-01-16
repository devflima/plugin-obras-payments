package br.com.pluginobras.payments.domain.model.liberacao;

import br.com.pluginobras.payments.domain.exception.RegraNegocioException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class LiberacaoPrestador {

    private UUID id;
    private String transacaoId;
    private String prestadorId;
    private BigDecimal valorLiberado;
    private LocalDateTime dataLiberacao;

    protected LiberacaoPrestador() {}

    public LiberacaoPrestador(
            String transacaoId,
            String prestadorId,
            BigDecimal valorLiberado
    ) {
        if(valorLiberado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Valor liberado deve ser maior que zero.");
        }

        this.id = UUID.randomUUID();
        this.transacaoId = transacaoId;
        this.prestadorId = prestadorId;
        this.valorLiberado = valorLiberado;
        this.dataLiberacao = LocalDateTime.now();
    }

}
