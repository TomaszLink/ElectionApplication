package pl.tomaszlink.electionapplication.elections.services;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
public class ElectionService {
    private final ElectionManager electionManager;
    private final ElectionResultsManager electionResultsManager;

    public ElectionEntity createElection(@NotNull ElectionCreateCommand command) {
        return this.electionManager.saveNewElection(command);
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
        return this.electionManager.updateElection(command);
    }
}
