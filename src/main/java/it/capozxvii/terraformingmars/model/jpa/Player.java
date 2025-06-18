package it.capozxvii.terraformingmars.model.jpa;

import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "player")
@Table(name = "player")
@IdClass(PlayerID.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player extends AbstractEntity {
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @OneToMany(mappedBy = "player")
    private Set<Points> pointsInGame;
}
