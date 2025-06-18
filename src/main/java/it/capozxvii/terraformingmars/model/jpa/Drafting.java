package it.capozxvii.terraformingmars.model.jpa;

import it.capozxvii.terraformingmars.model.jpa.converter.DraftingCorporationsForGamesConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "drafting")
@Table(name = "drafting")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Drafting extends AbstractEntity {

    @Convert(converter = DraftingCorporationsForGamesConverter.class)
    private Map<Integer, List<String>> draftings;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "player_nickname", referencedColumnName = "nickname"),
            @JoinColumn(name = "player_id", referencedColumnName = "id")
    })
    private Player player;

    @ManyToOne
    @JoinColumn(name = "championship")
    private Championship championship;
}
