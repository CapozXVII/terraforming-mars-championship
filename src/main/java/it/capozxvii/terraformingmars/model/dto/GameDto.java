package it.capozxvii.terraformingmars.model.dto;

import it.capozxvii.terraformingmars.model.jpa.Game;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for {@link Game}
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GameDto extends AbstractDto implements Serializable {
    private String location;
    private LocalDateTime gameDate;
    private Long championshipId;
    private List<PointsDto> points;
}
