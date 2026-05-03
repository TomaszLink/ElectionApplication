package pl.tomaszlink.electionapplication.elections.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.tomaszlink.electionapplication.api.ElectionsApi;
import pl.tomaszlink.electionapplication.elections.commands.ElectionCreateCommand;
import pl.tomaszlink.electionapplication.elections.commands.ElectionUpdateCommand;
import pl.tomaszlink.electionapplication.elections.converters.ElectionCommandConverter;
import pl.tomaszlink.electionapplication.elections.converters.ElectionConverter;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.elections.models.ElectionWithResults;
import pl.tomaszlink.electionapplication.elections.services.ElectionService;
import pl.tomaszlink.electionapplication.model.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class ElectionController implements ElectionsApi {
    private final ElectionService electionService;

    private static final String TOTAL_SIZE_HEADER = "Total-Size";

    @Override
    public ResponseEntity<ElectionResponse> createElection(CreateElectionRequest createElectionRequest) {
        ElectionCreateCommand command = ElectionCommandConverter.toElectionCreateCommand(createElectionRequest);
        ElectionEntity electionEntity = this.electionService.createElection(command);

        return ResponseEntity.status(201)
                .body(ElectionConverter.toElectionResponse(electionEntity));
    }

    @Override
    public ResponseEntity<ElectionResponse> getElection(UUID id) {
        ElectionWithResults electionWithResults = this.electionService.getElection(id);

        return ResponseEntity.ok(
                ElectionConverter.toElectionResponse(electionWithResults)
        );
    }

    @Override
    public ResponseEntity<List<ElectionListItemResponse>> getElections(Integer page, Integer size, String search, ElectionStatus status, String sortBy, String sortDirection) {
        pl.tomaszlink.electionapplication.elections.models.ElectionStatus electionStatus = status == null   ?   null    :   pl.tomaszlink.electionapplication.elections.models.ElectionStatus.valueOf(status.getValue());
        Page<ElectionListItemResponse> electionPage = this.electionService.getElections(page, size, search, electionStatus, sortBy, sortDirection)
                .map(ElectionConverter::toElectionListItemResponse);

        return ResponseEntity.ok()
                .header(TOTAL_SIZE_HEADER, String.valueOf(electionPage.getTotalElements()))
                .body(electionPage.getContent());
    }

    @Override
    public ResponseEntity<ElectionResponse> updateElection(UUID id, UpdateElectionRequest updateElectionRequest) {
        ElectionUpdateCommand electionUpdateCommand = ElectionCommandConverter.toElectionUpdateCommand(id, updateElectionRequest);
        ElectionEntity electionEntity = this.electionService.updateElection(electionUpdateCommand);

        return ResponseEntity.ok(
                ElectionConverter.toElectionResponse(electionEntity)
        );
    }
}
