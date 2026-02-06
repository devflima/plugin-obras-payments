package br.com.pluginobras.payments.application.service;

import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.TransacaoRepository;
import br.com.pluginobras.payments.interfaces.rest.dto.TransacaoCsvDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportacaoTransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public String gerarCsvTransacoes() {
        List<TransacaoEntity> transacoes = transacaoRepository.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append(cabecalho()).append("\n");

        transacoes.stream()
                .map(TransacaoCsvDTO::fromEntity)
                .forEach(dto -> csv.append(dto.toCsvLine()).append("\n"));

        return csv.toString();
    }

    public String cabecalho() {
        return String.join(";",
                "transacaoId",
                "clienteId",
                "prestadorId",
                "statusTransacao",
                "valorBase",
                "valorPagoCliente",
                "valorLiquidoPrestador",
                "receitaPlataforma",
                "dataCriacao",
                "dataPagamento",
                "dataLiberacao"
        );
    }

}
