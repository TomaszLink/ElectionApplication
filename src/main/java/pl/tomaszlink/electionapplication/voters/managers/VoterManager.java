package pl.tomaszlink.electionapplication.voters.managers;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;
import pl.tomaszlink.electionapplication.voters.exceptions.VoterAlreadyExistsException;
import pl.tomaszlink.electionapplication.voters.exceptions.VoterNotFoundException;
import pl.tomaszlink.electionapplication.voters.helpers.VoterSpecificationHelper;
import pl.tomaszlink.electionapplication.voters.models.UpdateVoterCommand;
import pl.tomaszlink.electionapplication.voters.properties.ConstraintsProperties;
import pl.tomaszlink.electionapplication.voters.repositories.VoterRepository;

import java.util.UUID;

@Component
@AllArgsConstructor
public class VoterManager {
    private final VoterRepository voterRepository;
    private final ConstraintsProperties constraintsProperties;

    public VoterEntity findById(@NotNull UUID id){
        return this.voterRepository.findById(id)
                .orElseThrow(() -> new VoterNotFoundException(String.format("Voter with id %s not found.", id)));
    }

    public Page<VoterEntity> getVotersPage(Integer page, Integer size, String search, Boolean blockedFilter, String sortBy, String sortDirection) {
        PageRequest pageRequest = PageRequest.of(page, size, createSort(sortBy, sortDirection));

        Specification<VoterEntity> specification = Specification
                .where(VoterSpecificationHelper.searchByName(search))
                .and(VoterSpecificationHelper.filterByBlocked(blockedFilter));
        return this.voterRepository.findAll(specification, pageRequest);
    }

    @Transactional
    public VoterEntity saveNewVoter(@NotNull VoterEntity voterEntity){
        return this.saveWithPeselUniqueCheck(voterEntity);
    }

    @Transactional
    public VoterEntity updateVoter(@NotNull UUID id, @NotNull UpdateVoterCommand updateVoterCommand){
        VoterEntity voterEntity = this.findById(id);
        voterEntity.updateDetailsData(
                updateVoterCommand.firstName(),
                updateVoterCommand.lastName(),
                updateVoterCommand.pesel(),
                updateVoterCommand.birthDate()
        );

        return this.saveWithPeselUniqueCheck(voterEntity);
    }

    @Transactional
    public VoterEntity updateVoterBlockStatus(@NotNull UUID id, boolean blocked){
        VoterEntity voterEntity = this.findById(id);
        voterEntity.updateBlockStatus(blocked);
        return this.voterRepository.save(voterEntity);
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection == null    ?
                Sort.Direction.ASC    :
                Sort.Direction.fromString(sortDirection);

        String property = sortBy == null    ?
                "lastName"  :
                sortBy;

        return Sort.by(direction, property);
    }

    private VoterEntity saveWithPeselUniqueCheck(@NotNull VoterEntity voterEntity){
        try {
            return this.voterRepository.saveAndFlush(voterEntity);
        } catch (DataIntegrityViolationException ex){
            if(ex.getMostSpecificCause().getMessage().contains(this.constraintsProperties.peselUnique())){
                throw new VoterAlreadyExistsException();
            }
            throw ex;
        }
    }
}
