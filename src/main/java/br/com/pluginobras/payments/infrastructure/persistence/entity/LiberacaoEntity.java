package br.com.pluginobras.payments.infrastructure.persistence.entity;

import br.com.pluginobras.payments.domain.model.liberacao.LiberacaoPrestador;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "liberacao_prestador")
public class LiberacaoEntity {

    @Id
    private UUID transacaoId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "transacao_id")
    private TransacaoEntity transacao;

    private String prestadorId;
    private BigDecimal valorLiberado;
    private LocalDateTime dataLiberacao;

    protected LiberacaoEntity() {}

    public static LiberacaoEntity fromDomain(LiberacaoPrestador domain, TransacaoEntity transacaoEntity) {
        LiberacaoEntity l = new LiberacaoEntity();

        l.transacao = transacaoEntity;

        l.prestadorId = domain.getPrestadorId();
        l.valorLiberado = domain.getValorLiberado();
        l.dataLiberacao = domain.getDataLiberacao();

        return l;
    }

}
