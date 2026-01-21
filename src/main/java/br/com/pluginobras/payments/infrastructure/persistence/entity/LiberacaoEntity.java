package br.com.pluginobras.payments.infrastructure.persistence.entity;

import br.com.pluginobras.payments.domain.model.liberacao.LiberacaoPrestador;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "liberacao_prestador")
public class LiberacaoEntity {

    @Id
    private UUID id;

    private String transacaoId;
    private String prestadorId;
    private BigDecimal valorLiberado;
    private LocalDateTime dataLiberacao;

    protected LiberacaoEntity() {}

    public static LiberacaoEntity fromDomain(LiberacaoPrestador domain) {
        LiberacaoEntity l = new LiberacaoEntity();

        l.id = domain.getId();
        l.transacaoId = domain.getTransacaoId();
        l.prestadorId = domain.getPrestadorId();
        l.valorLiberado = domain.getValorLiberado();
        l.dataLiberacao = domain.getDataLiberacao();

        return l;
    }

}
