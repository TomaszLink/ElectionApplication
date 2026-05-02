package pl.tomaszlink.electionapplication.elections.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import pl.tomaszlink.electionapplication.elections.models.ElectionOptionUpdateCommand;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "elections")
@Getter
public class ElectionEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "searchable", insertable = false, updatable = false)
    private String searchable;

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

    @Column(name = "options_size", nullable = false)
    private Integer optionsSize;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected ElectionEntity() {
    }

    private ElectionEntity(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate, int optionsSize) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.optionsSize = optionsSize;
    }

    public static ElectionEntity create(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate, int optionsSize) {
        return new ElectionEntity(name, description, startDate, endDate, optionsSize);
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

    public void update(String name, String description, OffsetDateTime startDate, OffsetDateTime endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(List<ElectionOptionUpdateCommand> options) {
        this.optionsSize = options.size();

        Map<UUID, ElectionOptionEntity> existingOptionsById = this.options.stream()
                .collect(Collectors.toMap(ElectionOptionEntity::getId, option -> option));

        Set<UUID> updatedOptionIds = options.stream()
                .map(ElectionOptionUpdateCommand::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        this.options.removeIf(option -> !updatedOptionIds.contains(option.getId()));

        for (ElectionOptionUpdateCommand optionRequest : options) {
            if (optionRequest.id() != null && existingOptionsById.containsKey(optionRequest.id())) {
                ElectionOptionEntity existingOption = existingOptionsById.get(optionRequest.id());
                existingOption.update(
                        optionRequest.name(),
                        optionRequest.description()
                );

            } else {
                ElectionOptionEntity.create(
                        optionRequest.name(),
                        optionRequest.description(),
                        this
                );
            }
        }
    }

    public void addOption(@NotNull ElectionOptionEntity option) {
        this.options.add(option);
    }

    public void markAsChanged() {
        this.updatedAt = OffsetDateTime.now();
    }

    public ElectionStatus getStatus(){
        OffsetDateTime now = OffsetDateTime.now();

        if(now.isBefore(startDate)){
            return ElectionStatus.DRAFT;
        }

        if(now.isBefore(endDate)){
            return ElectionStatus.ACTIVE;
        }

        return ElectionStatus.FINISHED;
    }
}
