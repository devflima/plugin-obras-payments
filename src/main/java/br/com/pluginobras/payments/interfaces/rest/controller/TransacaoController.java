package br.com.pluginobras.payments.interfaces.rest.controller;

import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.TransacaoRepository;
import br.com.pluginobras.payments.interfaces.rest.dto.CriarTransacaoRequest;
import br.com.pluginobras.payments.interfaces.rest.dto.CriarTransacaoResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoRepository repository;

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody CriarTransacaoRequest request) {

        TransacaoServico transacao = new TransacaoServico(
                request.getTransacaoId(),
                request.getClienteId(),
                request.getPrestadorId(),
                request.getValorBase(),
                request.getTaxaCliente(),
                request.getTaxaPrestador()
        );

        TransacaoEntity entity = TransacaoEntity.fromDomain(transacao);
        repository.save(entity);

        CriarTransacaoResponse response = new CriarTransacaoResponse(
                transacao.getTransacaoId(),
                transacao.getStatusTransacao().name(),
                transacao.getValorPagoCliente()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
