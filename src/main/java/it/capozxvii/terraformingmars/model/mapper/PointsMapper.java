package it.capozxvii.terraformingmars.model.mapper;

import it.capozxvii.terraformingmars.model.dto.PointsDto;
import it.capozxvii.terraformingmars.model.enums.corporation.Corporation;
import it.capozxvii.terraformingmars.model.enums.prelude.PreludeEnum;
import it.capozxvii.terraformingmars.model.jpa.Points;
import it.capozxvii.terraformingmars.util.CorporationFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PointsMapper extends CommonMapper {

    @Mapping(source = "corporation", target = "corporation", qualifiedByName = "getCorporation")
    @Mapping(source = "firstPrelude", target = "firstPrelude", qualifiedByName = "getPrelude")
    @Mapping(source = "secondPrelude", target = "secondPrelude", qualifiedByName = "getPrelude")
    Points toEntity(PointsDto pointsDto);


    @Mapping(source = "corporation", target = "corporation", qualifiedByName = "getCorporationName")
    @Mapping(source = "firstPrelude", target = "firstPrelude", qualifiedByName = "getPreludeName")
    @Mapping(source = "secondPrelude", target = "secondPrelude", qualifiedByName = "getPreludeName")
    PointsDto toDto(Points points);
    
    @Named("getCorporation")
    static Corporation getCorporation(final String corporationName) {
        return CorporationFactory.fromName(corporationName);
    }

    @Named("getPrelude")
    static PreludeEnum getPrelude(final String prelude) {
        return PreludeEnum.findPreludeByName(prelude);
    }

    @Named("getCorporationName")
    static String getCorporationName(final Corporation corporation) {
        return corporation.getName();
    }

    @Named("getPreludeName")
    static String getPreludeName(final PreludeEnum prelude) {
        return prelude.getName();
    }
}
