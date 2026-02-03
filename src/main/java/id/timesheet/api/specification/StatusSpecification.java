package id.timesheet.api.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import id.timesheet.api.dto.request.SearchRequest;
import id.timesheet.api.entity.Status;
import jakarta.persistence.criteria.Predicate;

public class StatusSpecification {

    public static Specification<Status> getSpecification(SearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.getQuery())) {
                Predicate queryPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("statusName")),
                        request.getQuery() + "%");
                predicates.add(queryPredicate);
            }

            if (predicates.isEmpty())
                return criteriaBuilder.conjunction();

            return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
        };
    }

}
