package br.com.pluginobras.payments.domain.model.transacao;

public enum StatusTransacao {
    CRIADA,
    AGUARDANDO_PAGAMENTO,
    PAGA,
    EM_ESCROW,
    EM_DISPUTA,
    LIBERADA,
    CONCLUIDA
}
