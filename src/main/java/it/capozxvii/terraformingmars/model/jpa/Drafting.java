package it.capozxvii.terraformingmars.model.jpa;

import it.capozxvii.terraformingmars.model.dto.CorporationsExpansionPairDto;
import it.capozxvii.terraformingmars.model.jpa.converter.DraftingCorporationsForGamesConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
public class Drafting {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "drafting_seq_gen"
    )
    @SequenceGenerator(
            name = "drafting_seq_gen",
            sequenceName = "drafting_seq",
            allocationSize = 1)
    @Column(name = "id")
    protected Long draftingId;

    @Convert(converter = DraftingCorporationsForGamesConverter.class)
    @Column(name = "draftings", columnDefinition = "jsonb")
    private Map<Long, CorporationsExpansionPairDto> draftings;

    @ManyToOne
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "championship")
    private Championship championship;
}
