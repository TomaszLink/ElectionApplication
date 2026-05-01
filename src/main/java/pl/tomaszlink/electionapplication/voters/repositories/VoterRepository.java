package pl.tomaszlink.electionapplication.voters.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;

import java.util.UUID;

@Repository
public interface VoterRepository extends JpaRepository<VoterEntity, UUID>, JpaSpecificationExecutor<VoterEntity> {

    boolean existsByPesel(String pesel);

    boolean existsByPeselAndIdNot(String pesel, UUID id);

    Page<VoterEntity> findBySearchableContaining(String search, Pageable pageable);
}
