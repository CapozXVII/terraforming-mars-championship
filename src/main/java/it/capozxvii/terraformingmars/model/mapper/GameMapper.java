package it.capozxvii.terraformingmars.model.mapper;

import it.capozxvii.terraformingmars.model.dto.GameDto;
import it.capozxvii.terraformingmars.model.jpa.Game;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = PointsMapper.class)
public interface GameMapper {

    @InheritInverseConfiguration
    Game toEntity(GameDto gameDto);

    @Mapping(source = "gameId", target = "id")
    @Mapping(source = "championship.championshipId", target = "championshipId")
    GameDto toDto(Game game);
}
