CREATE TABLE transacao_servico (
    id UUID PRIMARY KEY,
    cliente_id VARCHAR(50) NOT NULL,
    prestador_id VARCHAR(50) NOT NULL,

    valor_base DECIMAL(15,2) NOT NULL,
    taxa_cliente DECIMAL(15,2) NOT NULL,
    taxa_prestador DECIMAL(15,2) NOT NULL,
    valor_pago_cliente DECIMAL(15,2) NOT NULL,
    valor_liquido_prestador DECIMAL(15,2) NOT NULL,
    receita_plataforma DECIMAL(15,2) NOT NULL,

    status_transacao VARCHAR(30) NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    data_pagamento TIMESTAMP,
    data_liberacao TIMESTAMP
);

CREATE TABLE pagamento (
    transacao_id UUID PRIMARY KEY,
    meio_pagamento VARCHAR(20) NOT NULL,
    valor_bruto DECIMAL(15,2) NOT NULL,
    taxa_gateway DECIMAL(15,2) NOT NULL,
    valor_liquido DECIMAL(15,2) NOT NULL,
    status_pagamento VARCHAR(20) NOT NULL,
    data_evento TIMESTAMP NOT NULL,
    constraint fk_pagamento_transacao
        foreign key (transacao_id) references transacao_servico(id)
);

CREATE TABLE conta_escrow (
    transacao_id UUID PRIMARY KEY,
    prestador_id VARCHAR(50) NOT NULL,
    saldo_retido DECIMAL(15,2) NOT NULL,
    status_escrow VARCHAR(20) NOT NULL,
    constraint fk_escrow_transacao
        foreign key (transacao_id) references transacao_servico(id)
);

CREATE TABLE disputa_manual (
    transacao_id UUID PRIMARY KEY,
    valor_devolvido_cliente DECIMAL(15,2) NOT NULL,
    motivo VARCHAR(255),
    status_disputa VARCHAR(20) NOT NULL,
    data_abertura TIMESTAMP NOT NULL,
    data_resolucao TIMESTAMP,
    constraint fk_disputa_transacao
        foreign key (transacao_id) references transacao_servico(id)
);

CREATE TABLE liberacao_prestador (
    transacao_id UUID PRIMARY KEY,
    prestador_id VARCHAR(50) NOT NULL,
    valor_liberado DECIMAL(15,2) NOT NULL,
    data_liberacao TIMESTAMP NOT NULL,
    constraint fk_liberacao_transacao
        foreign key (transacao_id) references transacao_servico(id)
);

CREATE TABLE webhook_evento_processado (
    id_evento VARCHAR(100) PRIMARY KEY,
    data_processamento TIMESTAMP NOT NULL
);

