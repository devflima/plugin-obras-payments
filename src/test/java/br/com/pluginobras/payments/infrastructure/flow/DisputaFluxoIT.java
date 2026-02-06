package br.com.pluginobras.payments.infrastructure.flow;

import br.com.pluginobras.payments.domain.model.disputa.DisputaManual;
import br.com.pluginobras.payments.domain.model.disputa.StatusDisputa;
import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.infrastructure.persistence.entity.ContaEscrowEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.DisputaEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.ContaEscrowRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.DisputaRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.TransacaoRepository;
import br.com.pluginobras.payments.support.fake.PagamentoFake;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class DisputaFluxoIT {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaEscrowRepository escrowRepository;

    @Autowired
    private DisputaRepository disputaRepository;

    @Test
    void deveResolverDisputaParcial() {

        TransacaoServico transacao = new TransacaoServico(
                "C1",
                "P1",
                new BigDecimal("100"),
                new BigDecimal("10"),
                new BigDecimal("5")
        );

        TransacaoEntity transacaoEntity =
                transacaoRepository.save(TransacaoEntity.fromDomain(transacao));

        ContaEscrow escrowDomain =
                new ContaEscrow(transacao.getId(), transacao.getPrestadorId(), transacao.getValorLiquidoPrestador());

        ContaEscrowEntity escrowEntity =
                escrowRepository.save(ContaEscrowEntity.fromDomain(escrowDomain, transacaoEntity));

        DisputaManual disputaDomain =
                new DisputaManual(transacao.getId(), new BigDecimal("20"), "Problema");

        DisputaEntity disputaEntity =
                disputaRepository.save(DisputaEntity.fromDomain(disputaDomain, transacaoEntity));

        TransacaoServico domain = transacaoEntity.toDomain();

        ContaEscrow escrow = escrowEntity.toDomain();

        DisputaManual disputa = disputaEntity.toDomain();

        domain.registrarPagamento(PagamentoFake.pendente(transacao.getId()));

        domain.abrirDisputa(disputaDomain.getValorDevolvidoCliente(), disputaDomain.getMotivo());

        domain.resolverDisputa(escrow, disputa);

        escrowEntity.applyStateFromDomain(escrow);
        disputaEntity.applyStateFromDomain(disputa);
        transacaoEntity.applyStateFromDomain(domain);

        escrowRepository.save(escrowEntity);
        disputaRepository.save(disputaEntity);
        transacaoRepository.save(transacaoEntity);

        assertThat(disputa.getStatusDisputa()).isEqualTo(StatusDisputa.RESOLVIDA);
        assertThat(escrow.getSaldoRetido()).isEqualByComparingTo("75");
    }


}
