package pl.tomaszlink.electionapplication.voters.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.tomaszlink.electionapplication.api.VotersApi;
import pl.tomaszlink.electionapplication.model.*;
import pl.tomaszlink.electionapplication.voters.commands.RegisterVoterCommand;
import pl.tomaszlink.electionapplication.voters.commands.UpdateVoterBlockStatusCommand;
import pl.tomaszlink.electionapplication.voters.commands.UpdateVoterCommand;
import pl.tomaszlink.electionapplication.voters.converters.VoterCommandConverter;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;
import pl.tomaszlink.electionapplication.voters.converters.VoterConverter;
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
        VoterEntity voterEntity = this.voterService.getVoter(id);

        return ResponseEntity.ok(
                VoterConverter.toVoterResponse(voterEntity)
        );
    }

    @Override
    public ResponseEntity<List<VoterListItemResponse>> getVoters(Integer page, Integer size, String search, Boolean blockedFilter, String sortBy, String sortDirection) {
        Page<VoterListItemResponse> voterPage = this.voterService.getVoters(page, size, search, blockedFilter, sortBy, sortDirection)
                .map(VoterConverter::toVoterListItemResponse);

        return ResponseEntity.ok()
                .header(TOTAL_SIZE_HEADER, String.valueOf(voterPage.getTotalElements()))
                .body(voterPage.getContent());
    }

    @Override
    public ResponseEntity<VoterResponse> registerVoter(RegisterVoterRequest registerVoterRequest) {
        RegisterVoterCommand command = VoterCommandConverter.toRegisterVoterCommand(registerVoterRequest);
        VoterEntity voterEntity = this.voterService.registerVoter(command);

        return ResponseEntity.status(201)
                .body(VoterConverter.toVoterResponse(voterEntity));
    }

    @Override
    public ResponseEntity<VoterResponse> updateVoter(UUID id, UpdateVoterRequest updateVoterRequest) {
        UpdateVoterCommand command = VoterCommandConverter.toUpdateVoterCommand(id, updateVoterRequest);
        VoterEntity voterEntity = this.voterService.updateVoter(command);

        return ResponseEntity.ok(VoterConverter.toVoterResponse(voterEntity));
    }

    @Override
    public ResponseEntity<VoterResponse> updateVoterBlockStatus(UUID id, UpdateVoterBlockStatusRequest updateVoterBlockStatusRequest) {
        UpdateVoterBlockStatusCommand command = VoterCommandConverter.toUpdateVoterBlockStatusCommand(id, updateVoterBlockStatusRequest);
        VoterEntity voterEntity = this.voterService.updateVoterBlockStatus(command);

        return ResponseEntity.ok(
                VoterConverter.toVoterResponse(voterEntity)
        );
    }
}
