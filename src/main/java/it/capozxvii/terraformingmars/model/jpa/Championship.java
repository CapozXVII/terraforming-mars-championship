package it.capozxvii.terraformingmars.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Set;
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
public class Championship extends AbstractEntity {

    @Column(name = "championship_name")
    private String name;

    @Column(name = "starting_date")
    private LocalDateTime startingDate;

    @Column(name = "ending_date")
    private LocalDateTime endingDate;

    @OneToMany(mappedBy = "id")
    private Set<Game> games;
}
