package it.capozxvii.terraformingmars.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import it.capozxvii.terraformingmars.abstracts.AbstractTest;
import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;
import it.capozxvii.terraformingmars.model.dto.GameDto;
import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.model.dto.PointsDto;
import it.capozxvii.terraformingmars.model.dto.StandingDto;
import it.capozxvii.terraformingmars.model.mapper.ChampionshipMapper;
import it.capozxvii.terraformingmars.model.mapper.ChampionshipMapperImpl;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ChampionshipMapperTest extends AbstractTest {

    @Test
    void afterMappingTest() {

        ChampionshipMapper championshipMapper = new ChampionshipMapperImpl();


        PointsDto pointsDtoANickName = PointsDto.builder()
                .totalPoints(20)
                .player(PlayerDto.builder().nickname("aNickname").build()).build();

        PointsDto pointsDtoBNickName = PointsDto.builder()
                .totalPoints(15)
                .player(PlayerDto.builder().nickname("bNickname").build()).build();

        PointsDto pointsDtoCNickName = PointsDto.builder()
                .totalPoints(10)
                .player(PlayerDto.builder().nickname("cNickname").build()).build();
        GameDto gameDto1 =
                GameDto.builder().points(List.of(pointsDtoANickName, pointsDtoBNickName, pointsDtoCNickName)).build();

        pointsDtoANickName = PointsDto.builder()
                .totalPoints(55)
                .player(PlayerDto.builder().nickname("aNickname").build()).build();
        pointsDtoBNickName = PointsDto.builder()
                .totalPoints(30)
                .player(PlayerDto.builder().nickname("bNickname").build()).build();
        pointsDtoCNickName = PointsDto.builder()
                .totalPoints(45)
                .player(PlayerDto.builder().nickname("cNickname").build()).build();
        GameDto gameDto2 =
                GameDto.builder().points(List.of(pointsDtoANickName, pointsDtoBNickName, pointsDtoCNickName)).build();

        ChampionshipDto targetDto = ChampionshipDto.builder().games(Set.of(gameDto1, gameDto2)).build();
        championshipMapper.computeStanding(targetDto);

        StandingDto standingDto = targetDto.getStanding();
        assertNotNull(standingDto);

        Map<String, Long> standing = standingDto.getPlayersPoints();
        assertNotNull(standing);
        assertFalse(standing.isEmpty());
        assertEquals(75, standing.get("aNickname"));
        assertEquals(45, standing.get("bNickname"));
        assertEquals(55, standing.get("cNickname"));

        List<String> orderedKeys = standing.keySet().stream().toList();
        assertEquals("aNickname", orderedKeys.get(0));
        assertEquals("bNickname", orderedKeys.get(1));
        assertEquals("cNickname", orderedKeys.get(2));
    }

}
