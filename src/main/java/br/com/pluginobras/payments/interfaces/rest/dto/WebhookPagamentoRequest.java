package br.com.pluginobras.payments.interfaces.rest.dto;

import br.com.pluginobras.payments.domain.model.transacao.MeioPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebhookPagamentoRequest {

    private String transacaoId;
    private MeioPagamento meioPagamento;
    private BigDecimal valorPago;

}
