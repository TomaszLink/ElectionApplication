package pl.tomaszlink.electionapplication.voters.services;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.tomaszlink.electionapplication.model.*;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;
import pl.tomaszlink.electionapplication.voters.exceptions.VoterTooYoungException;
import pl.tomaszlink.electionapplication.voters.helpers.AdultPolicyHelper;
import pl.tomaszlink.electionapplication.voters.helpers.PeselHelper;
import pl.tomaszlink.electionapplication.voters.managers.VoterManager;
import pl.tomaszlink.electionapplication.voters.mappers.VoterConverter;
import pl.tomaszlink.electionapplication.voters.models.UpdateVoterCommand;

import java.util.UUID;

@Service
@AllArgsConstructor
public class VoterService {
    private final VoterManager voterManager;
    private final AdultPolicyHelper adultPolicyHelper;

    public VoterResponse getVoter(@NotNull UUID id) {
        return VoterConverter.toVoterResponse(
                this.voterManager.findById(id)
        );
    }

    public Page<VoterListItemResponse> getVoters(@NotNull Integer page, @NotNull Integer size, String search, Boolean blockedFilter, String sortBy, String sortDirection) {
        return this.voterManager.getVotersPage(page, size, search, blockedFilter, sortBy, sortDirection)
                .map(VoterConverter::toVoterListItemResponse);
    }

    public VoterResponse registerVoter(@NotNull RegisterVoterRequest registerVoterRequest) {
        VoterEntity voterEntity = VoterEntity.create(
                registerVoterRequest.getFirstName(),
                registerVoterRequest.getLastName(),
                registerVoterRequest.getPesel(),
                PeselHelper.extractBirthDateFromPesel(registerVoterRequest.getPesel())
        );

        if(!this.adultPolicyHelper.isAdult(voterEntity.getBirthDate())){
            throw new VoterTooYoungException();
        }

        return VoterConverter.toVoterResponse(
                this.voterManager.saveNewVoter(voterEntity)
        );
    }

    public VoterResponse updateVoter(@NotNull UUID id, @NotNull UpdateVoterRequest updateVoterRequest) {
        UpdateVoterCommand updateVoterCommand = new UpdateVoterCommand(
                updateVoterRequest.getFirstName(),
                updateVoterRequest.getLastName(),
                updateVoterRequest.getPesel(),
                PeselHelper.extractBirthDateFromPesel(updateVoterRequest.getPesel())
        );

        if(!this.adultPolicyHelper.isAdult(updateVoterCommand.birthDate())){
            throw new VoterTooYoungException();
        }

        return VoterConverter.toVoterResponse(
                this.voterManager.updateVoter(id, updateVoterCommand)
        );
    }

    public VoterResponse updateVoterBlockStatus(UUID id, UpdateVoterBlockStatusRequest updateVoterBlockStatusRequest) {
        return VoterConverter.toVoterResponse(
                this.voterManager.updateVoterBlockStatus(id, updateVoterBlockStatusRequest.getBlocked())
        );
    }
}
