package pl.tomaszlink.electionapplication.votes.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.elections.entities.ElectionOptionEntity;
import pl.tomaszlink.electionapplication.elections.entities.ElectionStatus;
import pl.tomaszlink.electionapplication.elections.managers.ElectionManager;
import pl.tomaszlink.electionapplication.entities.VoteEntity;
import pl.tomaszlink.electionapplication.model.VoteRequest;
import pl.tomaszlink.electionapplication.model.VoteResponse;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;
import pl.tomaszlink.electionapplication.voters.managers.VoterManager;
import pl.tomaszlink.electionapplication.votes.converters.VoteConverter;
import pl.tomaszlink.electionapplication.votes.exceptions.ElectionIsNotActiveException;
import pl.tomaszlink.electionapplication.votes.exceptions.ElectionOptionNotFoundException;
import pl.tomaszlink.electionapplication.votes.exceptions.VoterBlockedException;
import pl.tomaszlink.electionapplication.votes.managers.VoteManager;

import java.util.UUID;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteManager voteManager;
    private final ElectionManager electionManager;
    private final VoterManager voterManager;

    public VoteResponse vote(UUID id, VoteRequest voteRequest) {
        ElectionEntity electionEntity = this.electionManager.findById(id);

        if(electionEntity.getStatus() != ElectionStatus.ACTIVE){
            throw new ElectionIsNotActiveException();
        }

        VoterEntity voterEntity = this.voterManager.findById(voteRequest.getVoterId());

        if(voterEntity.isBlocked()){
            throw new VoterBlockedException(String.format("Voter with id %s is blocked.", voterEntity.getId()));
        }

        ElectionOptionEntity electionOptionEntity = electionEntity.getOptions().stream()
                .filter(option -> option.getId().equals(voteRequest.getOptionId()))
                .findFirst()
                .orElseThrow(() -> new ElectionOptionNotFoundException(String.format("Election option with id %s not found in election with id %s.", voteRequest.getOptionId(), id)));

        VoteEntity voteEntity = VoteEntity.create(electionEntity, voterEntity, electionOptionEntity);

        return VoteConverter.toVoteResponse(
                this.voteManager.save(voteEntity)
        );
    }
}
