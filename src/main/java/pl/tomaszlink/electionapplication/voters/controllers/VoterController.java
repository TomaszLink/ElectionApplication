package pl.tomaszlink.electionapplication.voters.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.tomaszlink.electionapplication.api.VotersApi;
import pl.tomaszlink.electionapplication.model.*;
import pl.tomaszlink.electionapplication.voters.services.VoterService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class VoterController implements VotersApi {

    private final VoterService voterService;

    private static final String TOTAL_SIZE_HEADER = "Total-Size";

    @Override
    public ResponseEntity<VoterResponse> getVoter(UUID id) {
        return ResponseEntity.ok(
                this.voterService.getVoter(id)
        );
    }

    @Override
    public ResponseEntity<List<VoterListItemResponse>> getVoters(Integer page, Integer size, String search, Boolean blockedFilter, String sortBy, String sortDirection) {
        Page<VoterListItemResponse> voterPage = this.voterService.getVoters(page, size, search, blockedFilter, sortBy, sortDirection);
        return ResponseEntity.ok()
                .header(TOTAL_SIZE_HEADER, String.valueOf(voterPage.getTotalElements()))
                .body(voterPage.getContent());
    }

    @Override
    public ResponseEntity<VoterResponse> registerVoter(RegisterVoterRequest registerVoterRequest) {
        return ResponseEntity.status(201)
                .body(
                        this.voterService.registerVoter(registerVoterRequest)
                );
    }

    @Override
    public ResponseEntity<VoterResponse> updateVoter(UUID id, UpdateVoterRequest updateVoterRequest) {
        return ResponseEntity.ok(
                this.voterService.updateVoter(id, updateVoterRequest)
        );
    }

    @Override
    public ResponseEntity<VoterResponse> updateVoterBlockStatus(UUID id, UpdateVoterBlockStatusRequest updateVoterBlockStatusRequest) {
        return ResponseEntity.ok(
                this.voterService.updateVoterBlockStatus(id, updateVoterBlockStatusRequest)
        );
    }
}
