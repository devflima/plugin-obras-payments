package br.com.pluginobras.payments.infrastructure.webhook;

import br.com.pluginobras.payments.config.JacksonTestConfig;
import br.com.pluginobras.payments.domain.model.transacao.MeioPagamento;
import br.com.pluginobras.payments.domain.model.transacao.TransacaoServico;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import br.com.pluginobras.payments.infrastructure.persistence.repository.ContaEscrowRepository;
import br.com.pluginobras.payments.infrastructure.persistence.repository.TransacaoRepository;
import br.com.pluginobras.payments.interfaces.rest.dto.WebhookPagamentoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(JacksonTestConfig.class)
@ActiveProfiles("test")
@Transactional
public class WebhookPagamentoIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaEscrowRepository escrowRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveProcessarPagamentoECriarEscrow() throws Exception {

        TransacaoServico transacao = new TransacaoServico(
                "C1",
                "P1",
                new BigDecimal("100"),
                new BigDecimal("10"),
                new BigDecimal("5")
        );

        transacaoRepository.save(TransacaoEntity.fromDomain(transacao));

        WebhookPagamentoRequest request = new WebhookPagamentoRequest(
                transacao.getId().toString(),
                MeioPagamento.PIX,
                new BigDecimal("110")
        );

        mockMvc.perform(post("/webhooks/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(escrowRepository.findByTransacaoId(transacao.getId())).isPresent();
    }

    @Test
    void deveSerIdempotente() throws Exception {
        TransacaoServico transacao = new TransacaoServico(
                "C1",
                "P1",
                new BigDecimal("100"),
                new BigDecimal("10"),
                new BigDecimal("5")
        );

        transacaoRepository.save(TransacaoEntity.fromDomain(transacao));

        WebhookPagamentoRequest request = new WebhookPagamentoRequest(
                transacao.getId().toString(),
                MeioPagamento.PIX,
                new BigDecimal("110")
        );

        mockMvc.perform(post("/webhooks/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/webhooks/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(escrowRepository.findAll()).hasSize(1);
    }
}
