package it.capozxvii.terraformingmars.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "championship")
@Table(name = "championship", uniqueConstraints = @UniqueConstraint(columnNames = "championship_name"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Championship {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "championship_seq_gen"
    )
    @SequenceGenerator(
            name = "championship_seq_gen",
            sequenceName = "championship_seq",
            allocationSize = 1)
    @Column(name = "id")
    protected Long championshipId;
    
    @Column(name = "championship_name")
    private String name;

    @Column(name = "starting_date")
    private LocalDateTime startingDate;

    @Column(name = "ending_date")
    private LocalDateTime endingDate;

    @OneToMany(mappedBy = "championship", fetch = FetchType.EAGER)
    private List<Game> games;

    public Championship(final Long championshipId, final String name) {
        this.championshipId = championshipId;
        this.name = name;
    }
}
