package it.capozxvii.terraformingmars.model.mapper;

import it.capozxvii.terraformingmars.model.dto.PointsDto;
import it.capozxvii.terraformingmars.model.jpa.Points;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PointsMapper extends CommonMapper {

    @Mapping(source = "corporation", target = "corporation", qualifiedByName = "getCorporation")
    @Mapping(source = "firstPrelude", target = "firstPrelude", qualifiedByName = "getPrelude")
    @Mapping(source = "secondPrelude", target = "secondPrelude", qualifiedByName = "getPrelude")
    @Mapping(target = "game", ignore = true)
    Points toEntity(PointsDto pointsDto);


    @Mapping(source = "corporation", target = "corporation", qualifiedByName = "getCorporationName")
    @Mapping(source = "firstPrelude", target = "firstPrelude", qualifiedByName = "getPreludeName")
    @Mapping(source = "secondPrelude", target = "secondPrelude", qualifiedByName = "getPreludeName")
    @Mapping(target = "game", ignore = true)
    PointsDto toDto(Points points);
}
