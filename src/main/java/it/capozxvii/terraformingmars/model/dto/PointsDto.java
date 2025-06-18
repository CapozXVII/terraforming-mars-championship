package it.capozxvii.terraformingmars.model.dto;

import it.capozxvii.terraformingmars.model.jpa.Points;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for {@link Points}
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PointsDto extends AbstractDto implements Serializable {
    private int terraformingRating;
    private int greenery;
    private int city;
    private int milestones;
    private int awards;
    private int cards;
    private Map<String, Integer> otherCategories;
    private String corporation;
    private String firstPrelude;
    private String secondPrelude;
    private PlayerDto player;
    private GameDto game;
    private int totalPoints;

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
