CREATE TABLE transacao_servico (
    id UUID PRIMARY KEY,
    transacao_id VARCHAR(50) NOT NULL,
    cliente_id VARCHAR(50) NOT NULL,
    prestador_id VARCHAR(50) NOT NULL,

    valor_base DECIMAL(15,2) NOT NULL,
    taxa_cliente DECIMAL(15,2) NOT NULL,
    taxa_prestador DECIMAL(15,2) NOT NULL,
    valor_pago_cliente DECIMAL(15,2) NOT NULL,
    valor_liquido_prestador DECIMAL(15,2) NOT NULL,
    receita_plataforma DECIMAL(15,2) NOT NULL,

    status VARCHAR(30) NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    data_pagamento TIMESTAMP,
    data_liberacao TIMESTAMP
);

CREATE TABLE pagamento (
    id UUID PRIMARY KEY,
    transacao_id VARCHAR(50) NOT NULL,
    meio_pagamento VARCHAR(20) NOT NULL,
    valor_bruto DECIMAL(15,2) NOT NULL,
    taxa_gateway DECIMAL(15,2) NOT NULL,
    valor_liquido DECIMAL(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_evento TIMESTAMP NOT NULL
);

CREATE TABLE conta_escrow (
    id UUID PRIMARY KEY,
    transacao_id VARCHAR(50) NOT NULL,
    prestador_id VARCHAR(50) NOT NULL,
    saldo_retido DECIMAL(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE disputa_manual (
    id UUID PRIMARY KEY,
    transacao_id VARCHAR(50) NOT NULL,
    valor_devolvido_cliente DECIMAL(15,2) NOT NULL,
    motivo VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    data_abertura TIMESTAMP NOT NULL,
    data_resolucao TIMESTAMP
);

CREATE TABLE liberacao_prestador (
    id UUID PRIMARY KEY,
    transacao_id VARCHAR(50) NOT NULL,
    prestador_id VARCHAR(50) NOT NULL,
    valor_liberado DECIMAL(15,2) NOT NULL,
    data_liberacao TIMESTAMP NOT NULL
);
