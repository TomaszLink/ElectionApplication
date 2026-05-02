package pl.tomaszlink.electionapplication.votes.converters;

import jakarta.validation.constraints.NotNull;
import pl.tomaszlink.electionapplication.entities.VoteEntity;
import pl.tomaszlink.electionapplication.model.VoteResponse;

public class VoteConverter {
    private VoteConverter() {}

    public static VoteResponse toVoteResponse(@NotNull VoteEntity voteEntity){
        return new VoteResponse()
                .id(voteEntity.getId())
                .electionId(voteEntity.getElection().getId())
                .optionId(voteEntity.getOption().getId())
                .voterId(voteEntity.getVoter().getId())
                .votedAt(voteEntity.getVotedAt());
    }
}
