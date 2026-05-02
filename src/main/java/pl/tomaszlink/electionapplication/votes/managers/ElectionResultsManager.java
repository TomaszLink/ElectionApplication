package pl.tomaszlink.electionapplication.votes.managers;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.tomaszlink.electionapplication.model.ElectionOptionResultResponse;
import pl.tomaszlink.electionapplication.model.ElectionResultsSummaryResponse;
import pl.tomaszlink.electionapplication.votes.repositories.VoteRepository;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ElectionResultsManager {
    private final VoteRepository voteRepository;

    @Transactional(readOnly = true)
    public ElectionResultsSummaryResponse getElectionResults(@NotNull UUID electionId){
        long totalVotes = this.voteRepository.countByElectionId(electionId);
        List<ElectionOptionResultResponse> items = this.voteRepository.getElectionResults(electionId)
                .stream()
                .map(result -> new ElectionOptionResultResponse()
                        .optionId(result.getOptionId())
                        .optionName(result.getOptionName())
                        .votesCount(result.getVotesCount())
                        .percentage(result.getPercentage().doubleValue())
                )
                .toList();

        return new ElectionResultsSummaryResponse()
                .totalVotes(totalVotes)
                .items(items);
    }
}
