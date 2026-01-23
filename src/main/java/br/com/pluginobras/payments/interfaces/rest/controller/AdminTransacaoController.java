package br.com.pluginobras.payments.interfaces.rest.controller;

import br.com.pluginobras.payments.domain.model.disputa.DisputaManual;
import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.liberacao.LiberacaoPrestador;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.infrastructure.persistence.entity.ContaEscrowEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.DisputaEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.LiberacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.ContaEscrowRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.DisputaRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.LiberacaoRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.TransacaoRepository;
import br.com.pluginobras.payments.interfaces.rest.dto.AbrirDisputaRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/admin/transacoes")
public class AdminTransacaoController {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaEscrowRepository escrowRepository;

    @Autowired
    private LiberacaoRepository liberacaoRepository;

    @Autowired
    private DisputaRepository disputaRepository;

    @PostMapping("/{transacaoId}/liberar")
    public ResponseEntity<Void> liberar(@PathVariable String transacaoId) {

        TransacaoEntity entity = transacaoRepository.findByTransacaoId(transacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"));

        TransacaoServico transacao = entity.toDomain();

        ContaEscrowEntity escrowEntity = escrowRepository.findByTransacaoId(transacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Escrow não encontrado"));

        ContaEscrow escrow = escrowEntity.toDomain();

        LiberacaoPrestador liberacao = transacao.liberarPagamento(escrow);

        escrowRepository.save(ContaEscrowEntity.fromDomain(escrow));
        liberacaoRepository.save(LiberacaoEntity.fromDomain(liberacao));
        transacaoRepository.save(TransacaoEntity.fromDomain(transacao));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{transacaoId}/disputa")
    public ResponseEntity<Void> abrirDisputa(@PathVariable String transacaoId, @RequestBody @Valid AbrirDisputaRequest request) {
        TransacaoEntity entity = transacaoRepository.findByTransacaoId(transacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"));

        TransacaoServico transacao = entity.toDomain();

        DisputaManual disputa = transacao.abrirDisputa(request.getValorDevolvidoCliente(), request.getMotivo());

        disputaRepository.save(DisputaEntity.fromDomain(disputa));
        transacaoRepository.save(TransacaoEntity.fromDomain(transacao));

        return ResponseEntity.ok().build();
    }

}
