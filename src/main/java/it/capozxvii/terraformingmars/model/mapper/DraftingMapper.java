package it.capozxvii.terraformingmars.model.mapper;

import it.capozxvii.terraformingmars.model.dto.CorporationsExpansionPairDto;
import it.capozxvii.terraformingmars.model.dto.DraftingDto;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Drafting;
import it.capozxvii.terraformingmars.model.jpa.Player;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DraftingMapper extends CommonMapper {


    @Mapping(source = "draftings", target = "draftings")
    Drafting toEntity(Player player, Championship championship, Map<Integer, CorporationsExpansionPairDto> draftings);

    @Mapping(source = "championship.championshipId", target = "championshipId")
    @Mapping(source = "player", target = "player")
    @Mapping(source = "player.playerId", target = "player.id")
    @Mapping(source = "draftings", target = "draftings")
    DraftingDto toDto(Drafting prevision);
    
}
