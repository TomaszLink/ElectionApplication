package pl.tomaszlink.electionapplication.results.managers;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.tomaszlink.electionapplication.results.models.ElectionOptionResult;
import pl.tomaszlink.electionapplication.results.models.ElectionOptionResultProjection;
import pl.tomaszlink.electionapplication.results.models.ElectionResultsSummary;
import pl.tomaszlink.electionapplication.votes.repositories.VoteRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ElectionResultsManager {
    private final VoteRepository voteRepository;

    @Transactional(readOnly = true)
    public ElectionResultsSummary getElectionResults(@NotNull UUID electionId){
        List<ElectionOptionResultProjection> electionResults = this.voteRepository.getElectionResults(electionId);

        long totalVotesCount = electionResults.stream().mapToLong(ElectionOptionResultProjection::getVotesCount).sum();

        List<ElectionOptionResult> results = electionResults.stream()
                .map(projection -> new ElectionOptionResult(
                        projection.getOptionId(),
                        projection.getOptionName(),
                        projection.getVotesCount(),
                        calculatePercentage(projection.getVotesCount(), totalVotesCount)
                ))
                .toList();

        return new ElectionResultsSummary(totalVotesCount, results);
    }

    private static Double calculatePercentage(long votesCount, long totalCount){
        if(totalCount == 0){
            return 0.0;
        }

        return BigDecimal.valueOf(votesCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
