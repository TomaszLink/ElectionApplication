package pl.tomaszlink.electionapplication.elections.helpers;

import org.springframework.data.jpa.domain.Specification;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.elections.models.ElectionStatus;

import java.time.OffsetDateTime;

public class ElectionSpecificationHelper {
    private ElectionSpecificationHelper(){}

    public static Specification<ElectionEntity> searchByName(String search){
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String normalizedSearch = "%" + search.trim().toLowerCase() + "%";
            return criteriaBuilder.like(
                    root.get("searchable"),
                    normalizedSearch
            );
        };
    }

    public static Specification<ElectionEntity> filterByStatus(ElectionStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }

            OffsetDateTime now = OffsetDateTime.now();

            if (status == ElectionStatus.DRAFT) {
                return criteriaBuilder.greaterThan(root.get("startDate"), now);
            }

            if (status == ElectionStatus.ACTIVE) {
                return criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), now),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), now)
                );
            }

            return criteriaBuilder.lessThan(root.get("endDate"), now);
        };
    }
}
