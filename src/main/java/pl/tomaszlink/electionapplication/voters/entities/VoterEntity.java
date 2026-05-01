package pl.tomaszlink.electionapplication.voters.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "voters")
@Getter
public class VoterEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "searchable", insertable = false, updatable = false)
    private String searchable;

    @Column(name = "pesel", nullable = false, unique = true, length = 11)
    private String pesel;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "blocked", nullable = false)
    private boolean blocked = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected VoterEntity() {
    }

    private VoterEntity(String firstName, String lastName, String pesel, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.birthDate = birthDate;
        this.blocked = false;
    }

    public static VoterEntity create(String firstName, String lastName, String pesel, LocalDate birthDate) {
        return new VoterEntity(firstName, lastName, pesel, birthDate);
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

    public void updateDetailsData(String firstName, String lastName, String pesel, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.birthDate = birthDate;
    }

    public void updateBlockStatus(boolean blocked) {
        this.blocked = blocked;
    }
}
