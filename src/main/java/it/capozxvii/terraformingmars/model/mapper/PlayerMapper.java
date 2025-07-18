package it.capozxvii.terraformingmars.model.mapper;

import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.model.jpa.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {


    @Mapping(source = "id", target = "playerId")
    Player toEntity(PlayerDto playerDto);

    @Mapping(source = "playerId", target = "id")
    PlayerDto toDto(Player player);

}
