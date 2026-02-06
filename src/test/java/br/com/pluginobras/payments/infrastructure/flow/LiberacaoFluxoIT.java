package br.com.pluginobras.payments.infrastructure.flow;

import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.escrow.StatusEscrow;
import br.com.pluginobras.payments.domain.model.pagamento.Pagamento;
import br.com.pluginobras.payments.domain.model.transacao.StatusTransacao;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.infrastructure.persistence.entity.ContaEscrowEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.ContaEscrowRepository;
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
public class LiberacaoFluxoIT {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaEscrowRepository escrowRepository;

    @Test
    void deveLiberarPagamento() {

        TransacaoServico transacaoCriada = new TransacaoServico(
                "C1",
                "P1",
                new BigDecimal("100"),
                new BigDecimal("10"),
                new BigDecimal("5")
        );

        TransacaoEntity transacaoEntity =
                transacaoRepository.save(
                        TransacaoEntity.fromDomain(transacaoCriada)
                );

        TransacaoServico transacaoDomainPagamento =
                transacaoEntity.toDomain();

        Pagamento pagamento = PagamentoFake.pendente(transacaoEntity.getId());

        ContaEscrow escrowDomain =
                transacaoDomainPagamento.registrarPagamento(pagamento);

        transacaoEntity.updateToDomain(transacaoDomainPagamento);
        transacaoRepository.save(transacaoEntity);

        ContaEscrowEntity escrowEntity =
                escrowRepository.save(
                        ContaEscrowEntity.fromDomain(
                                escrowDomain,
                                transacaoEntity
                        )
                );

        TransacaoEntity transacaoPersistida =
                transacaoRepository.findById(transacaoEntity.getId())
                        .orElseThrow();

        ContaEscrowEntity escrowPersistido =
                escrowRepository.findByTransacaoId(transacaoEntity.getId())
                        .orElseThrow();

        TransacaoServico transacaoDomainLiberacao =
                transacaoPersistida.toDomain();

        ContaEscrow escrowDomainLiberacao =
                escrowPersistido.toDomain();

        transacaoDomainLiberacao.liberarPagamento(escrowDomainLiberacao);

        transacaoPersistida.updateToDomain(transacaoDomainLiberacao);
        escrowPersistido.applyStateFromDomain(escrowDomainLiberacao);

        transacaoRepository.save(transacaoPersistida);
        escrowRepository.save(escrowPersistido);


        assertThat(transacaoDomainLiberacao.getStatusTransacao())
                .isEqualTo(StatusTransacao.LIBERADA);

        assertThat(transacaoDomainLiberacao.getDataLiberacao())
                .isNotNull();

        assertThat(escrowDomainLiberacao.getSaldoRetido())
                .isEqualByComparingTo("0");

        assertThat(escrowDomainLiberacao.getStatusEscrow())
                .isEqualTo(StatusEscrow.LIBERADO);
    }
}
