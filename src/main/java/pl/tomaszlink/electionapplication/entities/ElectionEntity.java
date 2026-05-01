package pl.tomaszlink.electionapplication.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "elections")
public class ElectionEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "start_date", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private OffsetDateTime endDate;

    @OneToMany(
            mappedBy = "election",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ElectionOptionEntity> options = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected ElectionEntity() {
    }

    private ElectionEntity(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate, List<ElectionOptionEntity> options) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.options = options;
    }

    public static ElectionEntity create(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate, List<ElectionOptionEntity> options) {
        return new ElectionEntity(name, description, startDate, endDate, options);
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

    public void update(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate, List<ElectionOptionEntity> options) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.options = options;
    }
}
