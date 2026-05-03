package pl.tomaszlink.electionapplication.elections.services;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.tomaszlink.electionapplication.elections.commands.ElectionCreateCommand;
import pl.tomaszlink.electionapplication.elections.models.ElectionStatus;
import pl.tomaszlink.electionapplication.elections.managers.ElectionManager;
import pl.tomaszlink.electionapplication.elections.commands.ElectionUpdateCommand;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.elections.models.ElectionWithResults;
import pl.tomaszlink.electionapplication.results.managers.ElectionResultsManager;
import pl.tomaszlink.electionapplication.results.models.ElectionResultsSummary;

import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class ElectionService {
    private final ElectionManager electionManager;
    private final ElectionResultsManager electionResultsManager;

    public ElectionEntity createElection(@NotNull ElectionCreateCommand command) {
        log.info("Creating new election");
        ElectionEntity electionEntity = this.electionManager.saveNewElection(command);
        log.info("Election has been successfully created with id {}", electionEntity.getId());
        return electionEntity;
    }

    public ElectionWithResults getElection(@NotNull UUID id) {
        ElectionEntity electionEntity = this.electionManager.findById(id);
        ElectionResultsSummary resultsSummary = electionEntity.getStatus() != ElectionStatus.DRAFT  ?
                this.electionResultsManager.getElectionResults(id)  :   null;

        return new ElectionWithResults(
                electionEntity,
                resultsSummary
        );
    }

    public Page<ElectionEntity> getElections(@NotNull Integer page, @NotNull Integer size, String search, ElectionStatus status, String sortBy, String sortDirection) {
        return this.electionManager.getElectionsPage(page, size, search, status, sortBy, sortDirection);
    }

    public ElectionEntity updateElection(@NotNull ElectionUpdateCommand command) {
        log.info("Updating election with id {}", command.id());
        ElectionEntity electionEntity = this.electionManager.updateElection(command);
        log.info("Election {} has been successfully updated", electionEntity.getId());
        return electionEntity;
    }
}
