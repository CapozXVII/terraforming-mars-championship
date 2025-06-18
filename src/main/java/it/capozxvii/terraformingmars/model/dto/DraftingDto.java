package it.capozxvii.terraformingmars.model.dto;

import it.capozxvii.terraformingmars.model.jpa.Drafting;
import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;
import java.io.Serializable;
import java.util.List;
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
public class DraftingDto extends AbstractDto implements Serializable {
    private PlayerID playerID;
    private Long championshipId;
    private Map<Integer, List<String>> draftings;
}
