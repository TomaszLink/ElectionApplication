package pl.tomaszlink.electionapplication.voters.services;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.tomaszlink.electionapplication.voters.commands.RegisterVoterCommand;
import pl.tomaszlink.electionapplication.voters.commands.UpdateVoterBlockStatusCommand;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;
import pl.tomaszlink.electionapplication.voters.helpers.AdultPolicyHelper;
import pl.tomaszlink.electionapplication.voters.managers.VoterManager;
import pl.tomaszlink.electionapplication.voters.commands.UpdateVoterCommand;

import java.util.UUID;

@Service
@AllArgsConstructor
public class VoterService {
    private final VoterManager voterManager;
    private final AdultPolicyHelper adultPolicyHelper;

    public VoterEntity getVoter(@NotNull UUID id) {
        return this.voterManager.findById(id);
    }

    public Page<VoterEntity> getVoters(@NotNull Integer page, @NotNull Integer size, String search, Boolean blockedFilter, String sortBy, String sortDirection) {
        return this.voterManager.getVotersPage(page, size, search, blockedFilter, sortBy, sortDirection);
    }

    public VoterEntity registerVoter(@NotNull RegisterVoterCommand command) {
        this.adultPolicyHelper.checkAgeMajority(command.birthDate());

        return this.voterManager.saveNewVoter(command);
    }

    public VoterEntity updateVoter(@NotNull UpdateVoterCommand command) {
        this.adultPolicyHelper.checkAgeMajority(command.birthDate());

        return this.voterManager.updateVoter(command);
    }

    public VoterEntity updateVoterBlockStatus(@NotNull UpdateVoterBlockStatusCommand command) {
        return this.voterManager.updateVoterBlockStatus(command);
    }
}
