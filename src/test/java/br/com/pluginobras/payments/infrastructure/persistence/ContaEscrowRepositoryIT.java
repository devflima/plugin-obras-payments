package br.com.pluginobras.payments.infrastructure.persistence;

import br.com.pluginobras.payments.domain.model.escrow.ContaEscrow;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.infrastructure.persistence.entity.ContaEscrowEntity;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.ContaEscrowRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.flyway.enabled=false"
})
public class ContaEscrowRepositoryIT {

    @Autowired
    private ContaEscrowRepository repository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Test
    void devePersistirEscrowComMesmoIdDaTransacao() {

        TransacaoServico transacao = new TransacaoServico(
            "C1",
            "P1",
            new BigDecimal("100"),
            new BigDecimal("10"),
            new BigDecimal("5")
        );

        TransacaoEntity entity = TransacaoEntity.fromDomain(transacao);

        transacaoRepository.save(entity);

        ContaEscrow escrow = new ContaEscrow(transacao.getId(), transacao.getPrestadorId(), transacao.getValorLiquidoPrestador());

        ContaEscrowEntity escrowEntity = ContaEscrowEntity.fromDomain(
                escrow,
                entity
        );

        repository.save(escrowEntity);

        ContaEscrowEntity salvo = repository.findById(transacao.getId()).orElseThrow();

        assertThat(salvo.toDomain().getTransacaoId()).isEqualTo(transacao.getId());
        assertThat(salvo.toDomain().getSaldoRetido()).isEqualByComparingTo("95");
    }
}
