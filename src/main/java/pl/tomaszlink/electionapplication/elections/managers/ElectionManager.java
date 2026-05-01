package pl.tomaszlink.electionapplication.elections.managers;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pl.tomaszlink.electionapplication.elections.exceptions.ElectionAlreadyStartedException;
import pl.tomaszlink.electionapplication.elections.exceptions.ElectionNotFoundException;
import pl.tomaszlink.electionapplication.elections.helpers.ElectionSpecificationHelper;
import pl.tomaszlink.electionapplication.elections.models.ElectionOptionUpdateCommand;
import pl.tomaszlink.electionapplication.elections.models.ElectionUpdateCommand;
import pl.tomaszlink.electionapplication.elections.repositories.ElectionRepository;
import pl.tomaszlink.electionapplication.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.model.ElectionStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ElectionManager {
    private final ElectionRepository electionRepository;

    private static final String DEFAULT_SORT_BY = "name";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    public ElectionEntity saveNewElection(@NotNull ElectionEntity electionEntity){
        return this.electionRepository.save(electionEntity);
    }

    public ElectionEntity findById(@NotNull UUID id){
        return this.electionRepository.findById(id)
                .orElseThrow(() -> new ElectionNotFoundException(String.format("Election with id %s not found.", id)));
    }

    public Page<ElectionEntity> getElectionsPage(@NotNull Integer page, @NotNull Integer size, String search, ElectionStatus status, String sortBy, String sortDirection) {
        PageRequest pageRequest = PageRequest.of(page, size, createSort(sortBy, sortDirection));

        Specification<ElectionEntity> specification = Specification
                .where(ElectionSpecificationHelper.searchByName(search))
                .and(ElectionSpecificationHelper.filterByStatus(status));

        return this.electionRepository.findAll(specification, pageRequest);
    }

    public ElectionEntity updateElection(@NotNull UUID id, @NotNull ElectionUpdateCommand electionUpdateCommand, List<ElectionOptionUpdateCommand> electionOptionUpdateCommands){
        ElectionEntity electionEntity = this.findById(id);

        if(electionEntity.getStartDate().isBefore(OffsetDateTime.now())){
            throw new ElectionAlreadyStartedException();
        }

        electionEntity.update(
                electionUpdateCommand.name(),
                electionUpdateCommand.description(),
                electionUpdateCommand.startDate(),
                electionUpdateCommand.endDate()
        );

        electionEntity.update(electionOptionUpdateCommands);

        return this.electionRepository.save(electionEntity);
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection == null    ?
                DEFAULT_SORT_DIRECTION    :
                Sort.Direction.fromString(sortDirection);

        String property = sortBy == null    ?
                DEFAULT_SORT_BY  :
                sortBy;

        return Sort.by(direction, property);
    }

}
