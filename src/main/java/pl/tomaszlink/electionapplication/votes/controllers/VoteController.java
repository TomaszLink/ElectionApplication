package pl.tomaszlink.electionapplication.votes.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.tomaszlink.electionapplication.api.VotesApi;
import pl.tomaszlink.electionapplication.model.VoteRequest;
import pl.tomaszlink.electionapplication.model.VoteResponse;
import pl.tomaszlink.electionapplication.votes.commands.VoteCommand;
import pl.tomaszlink.electionapplication.votes.converters.VoteCommandConverter;
import pl.tomaszlink.electionapplication.votes.converters.VoteConverter;
import pl.tomaszlink.electionapplication.votes.entities.VoteEntity;
import pl.tomaszlink.electionapplication.votes.services.VoteService;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class VoteController implements VotesApi {
    private final VoteService voteService;

    @Override
    public ResponseEntity<VoteResponse> vote(UUID id, VoteRequest voteRequest) {
        VoteCommand voteCommand = VoteCommandConverter.toVoteCommand(id, voteRequest);
        VoteEntity voteEntity = this.voteService.vote(voteCommand);
        return ResponseEntity.status(201)
                .body(
                        VoteConverter.toVoteResponse(voteEntity)
                );
    }
}
