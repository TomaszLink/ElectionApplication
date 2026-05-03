package pl.tomaszlink.electionapplication.votes.managers;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.tomaszlink.electionapplication.votes.entities.VoteEntity;
import pl.tomaszlink.electionapplication.votes.exceptions.VoteInElectionAlreadyExistsException;
import pl.tomaszlink.electionapplication.votes.properties.VoteConstraintsProperties;
import pl.tomaszlink.electionapplication.votes.repositories.VoteRepository;

@Component
@AllArgsConstructor
public class VoteManager {
    private final VoteRepository voteRepository;
    private final VoteConstraintsProperties properties;

    @Transactional
    public VoteEntity save(@NotNull VoteEntity voteEntity){
        try{
            return this.voteRepository.saveAndFlush(voteEntity);
        } catch (DataIntegrityViolationException ex){
            if(ex.getMostSpecificCause().getMessage().contains(this.properties.votePerElectionUnique())){
                throw new VoteInElectionAlreadyExistsException();
            }
            throw ex;
        }
    }

}
