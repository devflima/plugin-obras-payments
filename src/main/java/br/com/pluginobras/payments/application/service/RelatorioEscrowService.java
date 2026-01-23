package br.com.pluginobras.payments.application.service;

import br.com.pluginobras.payments.infrastructure.persistence.repository.ContaEscrowRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.DisputaRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.LiberacaoRepository;
import br.com.pluginobras.payments.interfaces.rest.dto.RelatorioEscrowPrestadorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioEscrowService {

    @Autowired
    ContaEscrowRepository escrowRepository;

    @Autowired
    LiberacaoRepository liberacaoRepository;

    @Autowired
    DisputaRepository disputaRepository;

    public List<RelatorioEscrowPrestadorResponse> gerarRelatorio() {

        Map<String, RelatorioEscrowPrestadorResponse> mapa = new HashMap<>();

        escrowRepository.totalRetidoPorPrestador().forEach(r -> {
            mapa.put(r.getPrestadorId(),
                    new RelatorioEscrowPrestadorResponse(
                            r.getPrestadorId(),
                            r.getTotalRetido(),
                            BigDecimal.ZERO,
                            BigDecimal.ZERO
                    )
            );
        });

        liberacaoRepository.totalLiberadoPorPrestador().forEach(l -> {
            mapa.computeIfAbsent(l.getPrestadorId(), id ->
                    new RelatorioEscrowPrestadorResponse(id, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
            ).setTotalLiberado(l.getTotalLiberado());
        });

        disputaRepository.totalEmDisputaPorPrestador().forEach(d -> {
            mapa.computeIfAbsent(d.getPrestadorId(), id ->
                    new RelatorioEscrowPrestadorResponse(id, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
            ).setTotalEmDisputa(d.getTotalEmDisputa());
        });

        return new ArrayList<>(mapa.values());
    }
}

