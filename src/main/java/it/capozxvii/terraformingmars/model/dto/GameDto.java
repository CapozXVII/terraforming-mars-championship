package it.capozxvii.terraformingmars.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.capozxvii.terraformingmars.model.jpa.Game;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameDto extends AbstractDto implements Serializable {
    private String location;
    private LocalDateTime gameDate;
    private Long championshipId;
    private List<PointsDto> points = new ArrayList<>();
}
