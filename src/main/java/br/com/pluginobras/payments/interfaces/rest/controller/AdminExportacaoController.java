package br.com.pluginobras.payments.interfaces.rest.controller;

import br.com.pluginobras.payments.application.service.ExportacaoTransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/admin/transacoes/export")
public class AdminExportacaoController {

    @Autowired
    private ExportacaoTransacaoService exportacaoService;

    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportacaoCsv() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String dataHora = LocalDateTime.now().format(formatter);

        String fileName = "transacoes-" + dataHora + ".csv";

        String csv = exportacaoService.gerarCsvTransacoes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv.getBytes(StandardCharsets.UTF_8));
    }
}
