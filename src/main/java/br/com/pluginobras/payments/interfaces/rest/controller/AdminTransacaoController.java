package br.com.pluginobras.payments.interfaces.rest.controller;

import br.com.pluginobras.payments.domain.model.disputa.DisputaManual;
import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.liberacao.LiberacaoPrestador;
import br.com.pluginobras.payments.domain.model.transacao.StatusTransacao;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.infrastructure.persistence.entity.ContaEscrowEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.DisputaEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.LiberacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.ContaEscrowRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.DisputaRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.LiberacaoRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.TransacaoRepository;
import br.com.pluginobras.payments.infrastructure.persistence.specification.TransacaoSpecification;
import br.com.pluginobras.payments.interfaces.rest.dto.AbrirDisputaRequest;
import br.com.pluginobras.payments.interfaces.rest.dto.TransacaoDetalheResponse;
import br.com.pluginobras.payments.interfaces.rest.dto.TransacaoResumoResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


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

    @PostMapping("/{transacaoId}/disputa/resolver")
    public ResponseEntity<Void> resolverDisputa(@PathVariable String transacaoId) {

        TransacaoEntity entity = transacaoRepository.findByTransacaoId(transacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"));

        TransacaoServico transacao = entity.toDomain();

        DisputaEntity disputaEntity = disputaRepository.findByTransacaoId(transacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disputa não encontrada"));

        ContaEscrowEntity escrowEntity = escrowRepository.findByTransacaoId(transacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Escrow não encontrado"));

        DisputaManual disputa = disputaEntity.toDomain();
        ContaEscrow escrow = escrowEntity.toDomain();

        transacao.resolverDisputa(escrow, disputa);

        escrowRepository.save(ContaEscrowEntity.fromDomain(escrow));
        disputaRepository.save(DisputaEntity.fromDomain(disputa));
        transacaoRepository.save(TransacaoEntity.fromDomain(transacao));

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<TransacaoResumoResponse>> listar(
            @RequestParam(required = false) StatusTransacao status,
            @RequestParam(required = false) String clienteId,
            @RequestParam(required = false) String prestadorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataCriacao") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        direction.equalsIgnoreCase("asc")
                                ? Sort.Direction.ASC
                                : Sort.Direction.DESC,
                        sort
                )
        );

        Page<TransacaoEntity> result =
                transacaoRepository.findAll(
                        TransacaoSpecification.filtrar(
                                status, clienteId, prestadorId
                        ),
                        pageable
                );

        Page<TransacaoResumoResponse> response =
                result.map(TransacaoResumoResponse::fromEntity);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{transacaoId}")
    public ResponseEntity<TransacaoDetalheResponse> detalhar(@PathVariable String transacaoId) {
        TransacaoEntity entity = transacaoRepository.findByTransacaoId(transacaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transação não encontrada"));

        return ResponseEntity.ok(TransacaoDetalheResponse.fromEntity(entity));
    }

}
