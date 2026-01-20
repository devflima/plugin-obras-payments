package br.com.pluginobras.payments.interfaces.rest.dto;

import br.com.pluginobras.payments.domain.model.transacao.MeioPagamento;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WebhookPagamentoRequest {

    private String transacaoId;
    private MeioPagamento meioPagamento;
    private BigDecimal valorPago;
    private String idEvento;

}
