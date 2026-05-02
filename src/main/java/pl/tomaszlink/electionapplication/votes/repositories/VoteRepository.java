package pl.tomaszlink.electionapplication.votes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.tomaszlink.electionapplication.entities.VoteEntity;
import pl.tomaszlink.electionapplication.votes.models.ElectionOptionResultProjection;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, String> {
    @Query(value = """
        SELECT
            eo.id AS optionId,
            eo.name AS optionName,
            COUNT(v.id) AS votesCount,
            CASE
                WHEN total.total_votes = 0 THEN 0
                ELSE ROUND((COUNT(v.id) * 100.0 / total.total_votes), 2)
            END AS percentage
        FROM election_options eo
        LEFT JOIN votes v
            ON v.option_id = eo.id
           AND v.election_id = eo.election_id
        CROSS JOIN (
            SELECT COUNT(*) AS total_votes
            FROM votes
            WHERE election_id = :electionId
        ) total
        WHERE eo.election_id = :electionId
        GROUP BY eo.id, eo.name, total.total_votes
        ORDER BY votesCount DESC, eo.name ASC
        """, nativeQuery = true)
    List<ElectionOptionResultProjection> getElectionResults(UUID electionId);

    long countByElectionId(UUID electionId);

}

