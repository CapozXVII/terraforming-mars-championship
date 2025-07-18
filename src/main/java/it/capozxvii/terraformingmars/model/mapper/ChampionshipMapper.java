package it.capozxvii.terraformingmars.model.mapper;

import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;
import it.capozxvii.terraformingmars.model.dto.StandingDto;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING, uses = {GameMapper.class})
public interface ChampionshipMapper {

    @Mapping(source = "id", target = "championshipId")
    Championship toEntity(ChampionshipDto championshipDto);

    @AfterMapping
    default void linkGames(@MappingTarget Championship championship) {
        if (championship.getGames() != null) {
            championship.getGames().forEach(game -> game.setGameId(championship.getChampionshipId()));
        }
    }

    @AfterMapping
    default void computeStanding(@MappingTarget ChampionshipDto target) {
        Map<String, Long> standing = new TreeMap<>();
        if (target.getGames() != null) {
            target.getGames().forEach(gameDto -> {
                gameDto.getPoints().forEach(pointsDto -> {
                    Optional.ofNullable(standing.get(pointsDto.getPlayer().getNickname()))
                            .ifPresentOrElse(
                                    point -> standing.put(pointsDto.getPlayer().getNickname(),
                                                          (long) pointsDto.getTotalPoints() + point),
                                    () -> standing.put(pointsDto.getPlayer().getNickname(),
                                                       (long) pointsDto.getTotalPoints()));
                });
            });
        }
        target.setStanding(StandingDto.builder().playersPoints(standing).build());
    }

    @Mapping(source = "championshipId", target = "id")
    ChampionshipDto toDto(Championship championship);
}
