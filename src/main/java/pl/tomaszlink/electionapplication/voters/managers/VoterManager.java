package pl.tomaszlink.electionapplication.voters.managers;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.tomaszlink.electionapplication.global.GlobalSortingQueryHelper;
import pl.tomaszlink.electionapplication.voters.commands.RegisterVoterCommand;
import pl.tomaszlink.electionapplication.voters.commands.UpdateVoterBlockStatusCommand;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;
import pl.tomaszlink.electionapplication.voters.exceptions.VoterAlreadyExistsException;
import pl.tomaszlink.electionapplication.voters.exceptions.VoterNotFoundException;
import pl.tomaszlink.electionapplication.voters.helpers.PeselHelper;
import pl.tomaszlink.electionapplication.voters.helpers.VoterSpecificationHelper;
import pl.tomaszlink.electionapplication.voters.commands.UpdateVoterCommand;
import pl.tomaszlink.electionapplication.voters.properties.VoterConstraintsProperties;
import pl.tomaszlink.electionapplication.voters.repositories.VoterRepository;

import java.util.UUID;

@Component
@AllArgsConstructor
public class VoterManager {
    private final VoterRepository voterRepository;
    private final VoterConstraintsProperties constraintsProperties;

    @Transactional(readOnly = true)
    public VoterEntity findById(@NotNull UUID id){
        return this.voterRepository.findById(id)
                .orElseThrow(() -> new VoterNotFoundException(String.format("Voter with id %s not found.", id)));
    }

    @Transactional(readOnly = true)
    public Page<VoterEntity> getVotersPage(Integer page, Integer size, String search, Boolean blockedFilter, String sortBy, String sortDirection) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                GlobalSortingQueryHelper.createVoterSort(sortBy, sortDirection)
        );

        Specification<VoterEntity> specification = Specification
                .where(VoterSpecificationHelper.searchByName(search))
                .and(VoterSpecificationHelper.filterByBlocked(blockedFilter));

        return this.voterRepository.findAll(specification, pageRequest);
    }

    @Transactional
    public VoterEntity saveNewVoter(@NotNull RegisterVoterCommand command){
        return this.saveWithPeselUniqueCheck(
                VoterEntity.create(
                        command.firstName(),
                        command.lastName(),
                        command.pesel(),
                        PeselHelper.extractBirthDateFromPesel(command.pesel())
                )
        );
    }

    @Transactional
    public VoterEntity updateVoter(@NotNull UpdateVoterCommand command){
        VoterEntity voterEntity = this.findById(command.id());
        voterEntity.updateDetailsData(
                command.firstName(),
                command.lastName(),
                command.pesel(),
                command.birthDate()
        );

        return this.saveWithPeselUniqueCheck(voterEntity);
    }

    @Transactional
    public VoterEntity updateVoterBlockStatus(@NotNull UpdateVoterBlockStatusCommand command){
        VoterEntity voterEntity = this.findById(command.id());
        voterEntity.updateBlockStatus(command.blocked());
        return this.voterRepository.saveAndFlush(voterEntity);
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
