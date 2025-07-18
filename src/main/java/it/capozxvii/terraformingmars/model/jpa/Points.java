package it.capozxvii.terraformingmars.model.jpa;

import it.capozxvii.terraformingmars.model.enums.corporation.Corporation;
import it.capozxvii.terraformingmars.model.enums.prelude.PreludeEnum;
import it.capozxvii.terraformingmars.model.jpa.converter.CorporationConverter;
import it.capozxvii.terraformingmars.model.jpa.converter.OtherCategoriesConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "points")
@Table(name = "points")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Points {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "points_seq_gen"
    )
    @SequenceGenerator(
            name = "points_seq_gen",
            sequenceName = "points_seq",
            allocationSize = 1)
    @Column(name = "id")
    protected Long pointsId;

    @Column(name = "terraforming_rating")
    @Builder.Default
    private int terraformingRating = 0;

    @Column(name = "greenery")
    @Builder.Default
    private int greenery = 0;

    @Column(name = "city")
    @Builder.Default
    private int city = 0;

    @Column(name = "milestones")
    @Builder.Default
    private int milestones = 0;

    @Column(name = "awards")
    @Builder.Default
    private int awards = 0;

    @Column(name = "cards")
    @Builder.Default
    private int cards = 0;

    @Column(name = "other_categories")
    @Convert(converter = OtherCategoriesConverter.class)
    private Map<String, Integer> otherCategories;

    @Convert(converter = CorporationConverter.class)
    private Corporation corporation;

    @Column(name = "first_prelude")
    @Enumerated(EnumType.STRING)
    private PreludeEnum firstPrelude;

    @Column(name = "second_prelude")
    @Enumerated(EnumType.STRING)
    private PreludeEnum secondPrelude;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "player_id", referencedColumnName = "id")
    })
    private Player player;

    @ManyToOne
    @JoinColumn(name = "game")
    private Game game;

    public int getTotalPoints() {
        return
                terraformingRating
                + greenery
                + city
                + milestones
                + awards
                + cards
                + (otherCategories != null
                   ? otherCategories.values().stream().mapToInt(Integer::valueOf).sum()
                   : 0);
    }
}
