package pl.tomaszlink.electionapplication.elections.converters;

import jakarta.validation.constraints.NotNull;
import pl.tomaszlink.electionapplication.elections.commands.ElectionCreateCommand;
import pl.tomaszlink.electionapplication.elections.commands.ElectionOptionCreateCommand;
import pl.tomaszlink.electionapplication.elections.commands.ElectionOptionUpdateCommand;
import pl.tomaszlink.electionapplication.elections.commands.ElectionUpdateCommand;
import pl.tomaszlink.electionapplication.model.CreateElectionRequest;
import pl.tomaszlink.electionapplication.model.UpdateElectionRequest;

import java.util.UUID;

public class ElectionCommandConverter {
    private ElectionCommandConverter(){}

    public static ElectionCreateCommand toElectionCreateCommand(@NotNull CreateElectionRequest createElectionRequest){
        return new ElectionCreateCommand(
                createElectionRequest.getName(),
                createElectionRequest.getDescription(),
                createElectionRequest.getStartDate(),
                createElectionRequest.getEndDate(),
                createElectionRequest.getOptions()
                        .stream()
                        .map(option -> new ElectionOptionCreateCommand(option.getName(), option.getDescription()))
                        .toList()
        );
    }

    public static ElectionUpdateCommand toElectionUpdateCommand(@NotNull UUID id, @NotNull UpdateElectionRequest updateElectionRequest){
        return new ElectionUpdateCommand(
                id,
                updateElectionRequest.getName(),
                updateElectionRequest.getDescription(),
                updateElectionRequest.getStartDate(),
                updateElectionRequest.getEndDate(),
                updateElectionRequest.getOptions().stream()
                        .map(option -> new ElectionOptionUpdateCommand(option.getId(), option.getName(), option.getDescription()))
                        .toList()
        );
    }
}
