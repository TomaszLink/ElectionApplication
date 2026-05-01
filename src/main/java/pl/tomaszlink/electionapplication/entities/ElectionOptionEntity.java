package pl.tomaszlink.electionapplication.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "election_options")
public class ElectionOptionEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "election_id",
            nullable = false
    )
    private ElectionEntity election;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected ElectionOptionEntity() {
    }

    private ElectionOptionEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static ElectionOptionEntity create(String name, String description) {
        return new ElectionOptionEntity(name, description);
    }

    public void update(String name, String description){
        this.name = name;
        this.description = description;
    }

    @PrePersist
    void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}
