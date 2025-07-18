package it.capozxvii.terraformingmars.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "game")
@Table(name = "game")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "game_seq_gen"
    )
    @SequenceGenerator(
            name = "game_seq_gen",
            sequenceName = "game_seq",
            allocationSize = 1)
    @Column(name = "id")
    protected Long gameId;

    @Column(name = "location")
    private String location;

    @Column(name = "game_date")
    private LocalDateTime gameDate;

    @ManyToOne
    @JoinColumn(name = "championship_id", nullable = false)
    private Championship championship;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Points> points;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return this.gameId != null && this.gameId.equals(((Game) o).getGameId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
