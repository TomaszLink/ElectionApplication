package pl.tomaszlink.electionapplication.voters.helpers;

import org.springframework.data.jpa.domain.Specification;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;

public class VoterSpecificationHelper {
    private VoterSpecificationHelper(){}

    public static Specification<VoterEntity> searchByName(String search) {
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

    public static Specification<VoterEntity> filterByBlocked(Boolean blockedFilter){
        return (root, query, criteriaBuilder) -> {
            if (blockedFilter == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("blocked"),
                    blockedFilter
            );
        };
    }
}
