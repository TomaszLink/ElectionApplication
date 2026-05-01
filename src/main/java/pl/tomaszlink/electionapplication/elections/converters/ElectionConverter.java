package pl.tomaszlink.electionapplication.elections.converters;

import jakarta.validation.constraints.NotNull;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.elections.entities.ElectionOptionEntity;
import pl.tomaszlink.electionapplication.model.ElectionListItemResponse;
import pl.tomaszlink.electionapplication.model.ElectionOptionResponse;
import pl.tomaszlink.electionapplication.model.ElectionResponse;
import pl.tomaszlink.electionapplication.model.ElectionStatus;

import java.time.OffsetDateTime;

public class ElectionConverter {
    private ElectionConverter(){}

    public static ElectionResponse toElectionResponse(@NotNull ElectionEntity electionEntity){
        return new ElectionResponse()
                .id(electionEntity.getId())
                .name(electionEntity.getName())
                .description(electionEntity.getDescription())
                .startDate(electionEntity.getStartDate())
                .endDate(electionEntity.getEndDate())
                .status(calculateStatus(electionEntity.getStartDate(), electionEntity.getEndDate()))
                .options(electionEntity.getOptions().stream()
                        .map(option -> new ElectionOptionResponse()
                                .id(option.getId())
                                .name(option.getName())
                                .description(option.getDescription())
                        )
                        .toList()
                );
    }

    public static ElectionListItemResponse toElectionListItemResponse(@NotNull ElectionEntity electionEntity){
        return new ElectionListItemResponse()
                .id(electionEntity.getId())
                .name(electionEntity.getName())
                .startDate(electionEntity.getStartDate())
                .endDate(electionEntity.getEndDate())
                .status(calculateStatus(electionEntity.getStartDate(), electionEntity.getEndDate()))
                .optionsCount(electionEntity.getOptionsSize());
    }

    private static ElectionOptionResponse toElectionOptionResponse(@NotNull ElectionOptionEntity electionOptionEntity){
        return new ElectionOptionResponse()
                .id(electionOptionEntity.getId())
                .name(electionOptionEntity.getName())
                .description(electionOptionEntity.getDescription());
    }

    private static ElectionStatus calculateStatus(@NotNull OffsetDateTime startDate, @NotNull OffsetDateTime endDate){
        OffsetDateTime now = OffsetDateTime.now();

        if(now.isBefore(startDate)){
            return ElectionStatus.DRAFT;
        }

        if(now.isBefore(endDate)){
            return ElectionStatus.ACTIVE;
        }

        return ElectionStatus.FINISHED;
    }
}
