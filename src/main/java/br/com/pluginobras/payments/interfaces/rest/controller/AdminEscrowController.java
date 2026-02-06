package br.com.pluginobras.payments.interfaces.rest.controller;

import br.com.pluginobras.payments.application.service.RelatorioEscrowService;
import br.com.pluginobras.payments.interfaces.rest.dto.RelatorioEscrowPrestadorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/escrow")
public class AdminEscrowController {

    @Autowired
    RelatorioEscrowService relatorioEscrowService;

    @GetMapping("/relatorio")
    public ResponseEntity<List<RelatorioEscrowPrestadorResponse>> relatorio() {
        return ResponseEntity.ok(relatorioEscrowService.gerarRelatorio());
    }

}
