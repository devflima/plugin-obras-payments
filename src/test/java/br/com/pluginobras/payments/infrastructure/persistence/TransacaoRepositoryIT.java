package br.com.pluginobras.payments.infrastructure.persistence;

import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
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
public class TransacaoRepositoryIT {

    @Autowired
    private TransacaoRepository repository;

    @Test
    void deveSalvarEBuscarTransacao() {
        TransacaoServico transacao = new TransacaoServico(
                "C1",
                "P1",
                new BigDecimal("100"),
                new BigDecimal("10"),
                new BigDecimal("5")
        );

        TransacaoEntity entity = TransacaoEntity.fromDomain(transacao);

        repository.save(entity);

        TransacaoEntity salvo = repository.findById(entity.getId()).orElseThrow();

        assertThat(salvo.getClienteId()).isEqualTo("C1");
        assertThat(salvo.getPrestadorId()).isEqualTo("P1");
    }
}
