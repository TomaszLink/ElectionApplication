package pl.tomaszlink.electionapplication.votes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.tomaszlink.electionapplication.results.models.ElectionOptionResultProjection;
import pl.tomaszlink.electionapplication.votes.entities.VoteEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, String> {
    @Query(value = """
    SELECT
        eo.id AS optionId,
        eo.name AS optionName,
        COUNT(v.id) AS votesCount
    FROM election_options eo
    LEFT JOIN votes v
        ON v.option_id = eo.id
       AND v.election_id = eo.election_id
    WHERE eo.election_id = :electionId
    GROUP BY eo.id, eo.name
    ORDER BY votesCount DESC, eo.name ASC
    """, nativeQuery = true)
    List<ElectionOptionResultProjection> getElectionResults(UUID electionId);
}

