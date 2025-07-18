package it.capozxvii.terraformingmars.model.mapper;

import it.capozxvii.terraformingmars.model.dto.DraftingDto;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Drafting;
import it.capozxvii.terraformingmars.model.jpa.Player;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DraftingMapper extends CommonMapper {

    Drafting toEntity(Player player, Championship championship, Map<Integer, List<String>> draftings);

    @Mapping(source = "championship.championshipId", target = "championshipId")
    @Mapping(source = "player.playerId", target = "playerId")
    DraftingDto toDto(Drafting prevision);
}
