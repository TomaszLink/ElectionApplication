package pl.tomaszlink.electionapplication.elections.converters;

import jakarta.validation.constraints.NotNull;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.elections.entities.ElectionOptionEntity;
import pl.tomaszlink.electionapplication.model.*;

import java.time.OffsetDateTime;

public class ElectionConverter {
    private ElectionConverter(){}

    public static ElectionResponse toElectionResponse(@NotNull ElectionEntity electionEntity, ElectionResultsSummaryResponse results){
        return toElectionResponse(electionEntity)
                .results(results);
    }

    public static ElectionResponse toElectionResponse(@NotNull ElectionEntity electionEntity){
        return new ElectionResponse()
                .id(electionEntity.getId())
                .name(electionEntity.getName())
                .description(electionEntity.getDescription())
                .startDate(electionEntity.getStartDate())
                .endDate(electionEntity.getEndDate())
                .status(convertElectionStatus(electionEntity.getStatus()))
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
                .status(convertElectionStatus(electionEntity.getStatus()))
                .optionsCount(electionEntity.getOptionsSize());
    }

    private static ElectionOptionResponse toElectionOptionResponse(@NotNull ElectionOptionEntity electionOptionEntity){
        return new ElectionOptionResponse()
                .id(electionOptionEntity.getId())
                .name(electionOptionEntity.getName())
                .description(electionOptionEntity.getDescription());
    }

    private static ElectionStatus convertElectionStatus(pl.tomaszlink.electionapplication.elections.entities.ElectionStatus electionStatus){
        return switch (electionStatus){
            case DRAFT -> ElectionStatus.DRAFT;
            case ACTIVE -> ElectionStatus.ACTIVE;
            case FINISHED -> ElectionStatus.FINISHED;
        };
    }
}
