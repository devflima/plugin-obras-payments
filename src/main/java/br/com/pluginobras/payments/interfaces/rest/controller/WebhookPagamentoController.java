package br.com.pluginobras.payments.interfaces.rest.controller;

import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.pagamento.Pagamento;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.infrastructure.gateway.pagamento.GatewayPagamentoClient;
import br.com.pluginobras.payments.infrastructure.gateway.pagamento.ResultadoPagamento;
import br.com.pluginobras.payments.infrastructure.persistence.entity.ContaEscrowEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.EventoProcessadoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.PagamentoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.ContaEscrowRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.EventoProcessadoRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.PagamentoRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.TransacaoRepository;
import br.com.pluginobras.payments.interfaces.rest.dto.WebhookPagamentoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/webhooks/pagamentos")
public class WebhookPagamentoController {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ContaEscrowRepository escrowRepository;

    @Autowired
    private EventoProcessadoRepository eventoRepository;

    @Autowired
    private GatewayPagamentoClient gateway;

    @PostMapping
    public ResponseEntity<Void> receberPagamento(@RequestBody WebhookPagamentoRequest request) {

        if(eventoRepository.existsById(request.getIdEvento())) {
            return ResponseEntity.ok().build();
        }

        TransacaoEntity entity = transacaoRepository.findById(UUID.fromString(request.getTransacaoId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"));

        TransacaoServico transacao = entity.toDomain();

        ResultadoPagamento resultado = gateway.processar(request.getMeioPagamento(), request.getValorPago());

        if(!resultado.isAprovado()) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).build();
        }

        Pagamento pagamento = new Pagamento(
                transacao.getTransacaoId(),
                request.getMeioPagamento(),
                resultado.getValorBruto(),
                resultado.getTaxaGateway()
        );

        ContaEscrow escrow = transacao.registrarPagamento(pagamento);

        pagamentoRepository.save(PagamentoEntity.fromDomain(pagamento));
        escrowRepository.save(ContaEscrowEntity.fromDomain(escrow));
        transacaoRepository.save(TransacaoEntity.fromDomain(transacao));

        eventoRepository.save(new EventoProcessadoEntity(request.getIdEvento(), LocalDateTime.now()));

        return ResponseEntity.ok().build();
    }

}
