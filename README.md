# Payments Service
Serviço responsável pelo processamento de pagamentos, gestão de transações, escrow e liberação de valores entre cliente, prestador e plataforma.
Este projeto foi desenvolvido com foco em domínio rico, regras de negócio bem definidas, testes de domínio e integração, e separação clara entre camadas.

---

# 💡 Problema Resolvido

Sistemas de pagamento frequentemente enfrentam desafios como:

- Inconsistência de estado
- Eventos duplicados (webhooks)
- Regras financeiras complexas
- Transições inválidas de transação

Este serviço foi projetado para garantir:

✔ Consistência de estado  
✔ Idempotência em operações críticas  
✔ Proteção das regras de negócio financeiras  
✔ Separação clara entre domínio e infraestrutura  

---

# 🧠 Visão Geral

O Payments Service gerencia todo o ciclo financeiro de uma transação:

- Criação da transação
- Recebimento de pagamento via webhook
- Confirmação de pagamento
- Criação e gestão de conta escrow
- Resolução de disputas
- Liberação de valores

O design prioriza:

✔ Consistência de estado  
✔ Idempotência  
✔ Regras de negócio protegidas por testes  
✔ Facilidade de evolução 

---

# 🎯 Decisões de Arquitetura

O projeto segue uma abordagem inspirada em DDD (Domain-Driven Design):

- Domínio isolado de frameworks
- Estados protegidos por regras explícitas
- Idempotência tratada no nível de domínio
- Separação clara entre regras de negócio e infraestrutura
- Testes focados em comportamento

---

# 🧱 Arquitetura
O projeto segue uma abordagem inspirada em DDD (Domain-Driven Design):

```text
br.com.pluginobras.payments
├── application
│   ├── dto / service
├── domain
│   ├── model
│   ├── exception
│   └── service
├── infrastructure
│   ├── persistence
│   │   └── entity / repository
│   ├── webhook
│   └── flow
├── interfaces
│   └── rest
└── test
├── domain
└── integration
```

---

# ⭐ Diferenciais Técnicos
✔ Modelagem explícita de estados de transação
✔ Proteção contra transições inválidas
✔ Idempotência em operações críticas
✔ Testes de domínio independentes de Spring
✔ Arquitetura inspirada em DDD

---

# 🔄 Fluxo Principal de Pagamento
- Transação é criada com status CRIADA
- Gateway externo envia webhook de pagamento
- Pagamento é registrado e confirmado
- Conta escrow é criada
- Transação muda para estado PAGA
- Valores ficam retidos até liberação ou disputa

---

# 🧪 Estratégia de Testes
## Testes de Domínio
- Validam regras de negócio puras
- Não utilizam Entity, Repository ou Spring
- Garantem:
    - Estados válidos
    - Transições corretas
    - Exceções de negócio
### Exemplos:
- Confirmação de pagamento
- Estados inválidos
- Idempotência no domínio
### Testes de Integração
- Validam fluxo completo
- Utilizam banco em memória
- Exercitam:
    - Webhooks
    - Persistência
    - Orquestração entre camadas

---

# 🚀 Tecnologias Utilizadas
- Java 17+
- Spring Boot
- Spring Data JPA
- H2 (testes)
- JUnit 5
- Mockito
- MockMvc
- Jackson

---

# ▶️ Como Executar o Projeto
## Pré-requisitos
- Java 17+
- Maven

### Executar testes
```text
 mvn test
```

### Executar aplicação
```text
 mvn spring-boot:run
```

---

# 🧩 Estado Atual do Projeto
```text
✔️ Regras de domínio implementadas
✔️ Testes de domínio concluídos
✔️ Testes de integração concluídos
✔️ Webhook de pagamento funcional
✔️ Escrow e liberação validados
```

---

# Próximos passos (roadmap)
- Observabilidade (Prometheus + Grafana)
- Retry + DLQ para webhooks
- Mensageria (RabbitMQ)
- Orquestração com Kubernetes
- Segurança e autenticação

# 🧑‍💻 Autor
#### Felipe Lima
