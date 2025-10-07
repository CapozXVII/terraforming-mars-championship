package it.capozxvii.terraformingmars.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.capozxvii.terraformingmars.model.jpa.Drafting;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for {@link Drafting}
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DraftingDto extends AbstractDto implements Serializable {
    private PlayerDto player;
    private Long championshipId;
    private Map<Integer, CorporationsExpansionPairDto> draftings;
}
