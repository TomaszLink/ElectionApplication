package pl.tomaszlink.electionapplication.elections.services;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.tomaszlink.electionapplication.elections.converters.ElectionConverter;
import pl.tomaszlink.electionapplication.elections.managers.ElectionManager;
import pl.tomaszlink.electionapplication.elections.models.ElectionOptionUpdateCommand;
import pl.tomaszlink.electionapplication.elections.models.ElectionUpdateCommand;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.elections.entities.ElectionOptionEntity;
import pl.tomaszlink.electionapplication.model.*;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ElectionService {
    private final ElectionManager electionManager;

    public ElectionResponse createElection(@NotNull CreateElectionRequest createElectionRequest) {
        ElectionEntity electionEntity = ElectionEntity.create(
                createElectionRequest.getName(),
                createElectionRequest.getDescription(),
                createElectionRequest.getStartDate(),
                createElectionRequest.getEndDate(),
                createElectionRequest.getOptions().size()
        );

        createElectionRequest.getOptions()
                .forEach(option -> ElectionOptionEntity.create(
                        option.getName(),
                        option.getDescription(),
                        electionEntity
                ));

        return ElectionConverter.toElectionResponse(
                this.electionManager.saveNewElection(electionEntity)
        );
    }

    public ElectionResponse getElection(@NotNull UUID id) {
        return ElectionConverter.toElectionResponse(
                this.electionManager.findById(id)
        );
    }

    public Page<ElectionListItemResponse> getElections(@NotNull Integer page, @NotNull Integer size, String search, ElectionStatus status, String sortBy, String sortDirection) {
        return this.electionManager.getElectionsPage(page, size, search, status, sortBy, sortDirection)
                .map(ElectionConverter::toElectionListItemResponse);
    }

    public ElectionResponse updateElection(@NotNull UUID id, @NotNull UpdateElectionRequest updateElectionRequest) {
        ElectionUpdateCommand electionUpdateCommand = new ElectionUpdateCommand(
                updateElectionRequest.getName(),
                updateElectionRequest.getDescription(),
                updateElectionRequest.getStartDate(),
                updateElectionRequest.getEndDate()
        );

        List<ElectionOptionUpdateCommand> electionOptionUpdateCommands = updateElectionRequest.getOptions()
                .stream()
                .map(option -> new ElectionOptionUpdateCommand(
                        option.getId(),
                        option.getName(),
                        option.getDescription())
                ).toList();
        return ElectionConverter.toElectionResponse(
                this.electionManager.updateElection(id, electionUpdateCommand, electionOptionUpdateCommands)
        );
    }
}
