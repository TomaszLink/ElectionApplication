package pl.tomaszlink.electionapplication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pl.tomaszlink.electionapplication.elections.entities.ElectionEntity;
import pl.tomaszlink.electionapplication.elections.entities.ElectionOptionEntity;
import pl.tomaszlink.electionapplication.voters.entities.VoterEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "votes")
@Getter
public class VoteEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "election_id",
            nullable = false
    )
    private ElectionEntity election;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "voter_id",
            nullable = false
    )
    private VoterEntity voter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "option_id",
            nullable = false
    )
    private ElectionOptionEntity option;

    @Column(name = "voted_at", nullable = false, updatable = false)
    private OffsetDateTime votedAt;

    protected VoteEntity() {
    }

    private VoteEntity(ElectionEntity election, VoterEntity voter, ElectionOptionEntity option) {
        this.election = election;
        this.voter = voter;
        this.option = option;
    }

    public static VoteEntity create(ElectionEntity election, VoterEntity voter, ElectionOptionEntity option) {
        return new VoteEntity(election, voter, option);
    }

    @PrePersist
    void prePersist() {
        this.votedAt = OffsetDateTime.now();
    }
}
