package pl.tomaszlink.electionapplication.elections.managers;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.tomaszlink.electionapplication.elections.commands.ElectionCreateCommand;
import pl.tomaszlink.electionapplication.elections.entities.ElectionOptionEntity;
import pl.tomaszlink.electionapplication.elections.models.ElectionStatus;
import pl.tomaszlink.electionapplication.elections.exceptions.ElectionAlreadyStartedException;
import pl.tomaszlink.electionapplication.elections.exceptions.ElectionNotFoundException;
import pl.tomaszlink.electionapplication.elections.helpers.ElectionSpecificationHelper;
import pl.tomaszlink.electionapplication.elections.commands.ElectionUpdateCommand;
import pl.tomaszlink.electionapplication.elections.repositories.ElectionRepository;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.global.GlobalSortingQueryHelper;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ElectionManager {
    private final ElectionRepository electionRepository;


    @Transactional
    public ElectionEntity saveNewElection(@NotNull ElectionCreateCommand command){
        ElectionEntity electionEntity = ElectionEntity.create(
                command.name(),
                command.description(),
                command.startDate(),
                command.endDate(),
                command.options().size()
        );

        command.options()
                .forEach(option -> ElectionOptionEntity.create(
                        option.name(),
                        option.description(),
                        electionEntity
                ));

        return this.electionRepository.save(electionEntity);
    }

    @Transactional(readOnly = true)
    public ElectionEntity findById(@NotNull UUID id){
        return this.electionRepository.findById(id)
                .orElseThrow(() -> new ElectionNotFoundException(String.format("Election with id %s not found.", id)));
    }

    @Transactional(readOnly = true)
    public Page<ElectionEntity> getElectionsPage(@NotNull Integer page, @NotNull Integer size, String search, ElectionStatus status, String sortBy, String sortDirection) {
        PageRequest pageRequest = PageRequest.of(page, size, GlobalSortingQueryHelper.createElectionSort(sortBy, sortDirection));

        Specification<ElectionEntity> specification = Specification
                .where(ElectionSpecificationHelper.searchByName(search))
                .and(ElectionSpecificationHelper.filterByStatus(status));

        return this.electionRepository.findAll(specification, pageRequest);
    }

    @Transactional
    public ElectionEntity updateElection(@NotNull ElectionUpdateCommand command){
        ElectionEntity electionEntity = this.findById(command.id());

        if(electionEntity.getStartDate().isBefore(OffsetDateTime.now())){
            throw new ElectionAlreadyStartedException();
        }

        electionEntity.update(
                command.name(),
                command.description(),
                command.startDate(),
                command.endDate()
        );

        electionEntity.update(command.options());

        electionEntity.markAsChanged();

        return this.electionRepository.save(electionEntity);
    }

}
