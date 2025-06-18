package it.capozxvii.terraformingmars.model.dto;

import it.capozxvii.terraformingmars.model.jpa.Championship;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for {@link Championship}
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChampionshipDto extends AbstractDto implements Serializable {
    private String name;
    private LocalDateTime startingDate;
    private LocalDateTime endingDate;
    private Set<GameDto> games;
}
