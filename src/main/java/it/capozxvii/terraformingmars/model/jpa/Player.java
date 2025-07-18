package it.capozxvii.terraformingmars.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "player")
@Table(name = "player")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "player_seq_gen"
    )
    @SequenceGenerator(
            name = "player_seq_gen",
            sequenceName = "player_seq",
            allocationSize = 1)
    @Column(name = "id")
    protected Long playerId;
    
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @OneToMany(mappedBy = "player")
    private Set<Points> pointsInGame;
}
