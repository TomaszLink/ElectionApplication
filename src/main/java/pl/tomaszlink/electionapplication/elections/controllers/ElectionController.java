package pl.tomaszlink.electionapplication.elections.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.tomaszlink.electionapplication.api.ElectionsApi;
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
        return ResponseEntity.status(201)
                .body(this.electionService.createElection(
                        createElectionRequest
                ));
    }

    @Override
    public ResponseEntity<ElectionResponse> getElection(UUID id) {
        return ResponseEntity.ok(
                this.electionService.getElection(id)
        );
    }

    @Override
    public ResponseEntity<List<ElectionListItemResponse>> getElections(Integer page, Integer size, String search, ElectionStatus status, String sortBy, String sortDirection) {
        Page<ElectionListItemResponse> electionPage = this.electionService.getElections(page, size, search, status, sortBy, sortDirection);
        return ResponseEntity.ok()
                .header(TOTAL_SIZE_HEADER, String.valueOf(electionPage.getTotalElements()))
                .body(electionPage.getContent());
    }

    @Override
    public ResponseEntity<ElectionResponse> updateElection(UUID id, UpdateElectionRequest updateElectionRequest) {
        return ResponseEntity.ok(
                this.electionService.updateElection(id, updateElectionRequest)
        );
    }
}
