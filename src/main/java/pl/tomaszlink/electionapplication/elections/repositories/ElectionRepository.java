package pl.tomaszlink.electionapplication.elections.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;

import java.util.UUID;

@Repository
public interface ElectionRepository extends JpaRepository<ElectionEntity, UUID>, JpaSpecificationExecutor<ElectionEntity> {
}
