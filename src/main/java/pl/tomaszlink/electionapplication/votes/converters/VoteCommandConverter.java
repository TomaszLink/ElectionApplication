package pl.tomaszlink.electionapplication.votes.converters;

import jakarta.validation.constraints.NotNull;
import pl.tomaszlink.electionapplication.model.VoteRequest;
import pl.tomaszlink.electionapplication.votes.commands.VoteCommand;

import java.util.UUID;

public class VoteCommandConverter {
    private VoteCommandConverter(){}

    public static VoteCommand toVoteCommand(@NotNull UUID electionId, @NotNull VoteRequest voteRequest){
        return new VoteCommand(
                electionId,
                voteRequest.getVoterId(),
                voteRequest.getOptionId()
        );
    }
}
