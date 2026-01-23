package br.com.pluginobras.payments.infrastructure.persistence.specification;

import br.com.pluginobras.payments.domain.model.transacao.StatusTransacao;
import br.com.pluginobras.payments.infrastructure.persistence.entity.TransacaoEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransacaoSpecification {

    public static Specification<TransacaoEntity> filtrar(
            StatusTransacao status,
            String clienteId,
            String prestadorId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(
                        root.get("statusTransacao"), status.name()
                ));
            }

            if (clienteId != null) {
                predicates.add(cb.equal(root.get("clienteId"), clienteId));
            }

            if (prestadorId != null) {
                predicates.add(cb.equal(root.get("prestadorId"), prestadorId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

