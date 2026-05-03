package pl.tomaszlink.electionapplication.votes.services;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.elections.entities.ElectionOptionEntity;
import pl.tomaszlink.electionapplication.elections.models.ElectionStatus;
import pl.tomaszlink.electionapplication.elections.managers.ElectionManager;
import pl.tomaszlink.electionapplication.votes.commands.VoteCommand;
import pl.tomaszlink.electionapplication.votes.entities.VoteEntity;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;
import pl.tomaszlink.electionapplication.voters.managers.VoterManager;
import pl.tomaszlink.electionapplication.votes.exceptions.ElectionIsNotActiveException;
import pl.tomaszlink.electionapplication.votes.exceptions.ElectionOptionNotFoundException;
import pl.tomaszlink.electionapplication.votes.exceptions.VoterBlockedException;
import pl.tomaszlink.electionapplication.votes.managers.VoteManager;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteManager voteManager;
    private final ElectionManager electionManager;
    private final VoterManager voterManager;

    public VoteEntity vote(@NotNull VoteCommand command) {
        VoterEntity voterEntity = this.voterManager.findById(command.voterId());
        if(voterEntity.isBlocked()){
            throw new VoterBlockedException(String.format("Voter with id %s is blocked.", voterEntity.getId()));
        }

        ElectionEntity electionEntity = this.electionManager.findById(command.electionId());
        if(electionEntity.getStatus() != ElectionStatus.ACTIVE){
            throw new ElectionIsNotActiveException();
        }

        ElectionOptionEntity electionOptionEntity = electionEntity.getOptions().stream()
                .filter(option -> option.getId().equals(command.electionId()))
                .findFirst()
                .orElseThrow(() -> new ElectionOptionNotFoundException(String.format("Election option with id %s not found in election with id %s.", command.optionId(), command.electionId())));

        VoteEntity voteEntity = VoteEntity.create(electionEntity, voterEntity, electionOptionEntity);

        return this.voteManager.save(voteEntity);
    }
}
