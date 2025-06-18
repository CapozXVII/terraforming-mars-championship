package it.capozxvii.terraformingmars.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.capozxvii.terraformingmars.abstracts.AbstractServiceTest;
import it.capozxvii.terraformingmars.model.dto.PointsDto;
import it.capozxvii.terraformingmars.model.enums.corporation.ColoniesCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.PreludeCorporations;
import it.capozxvii.terraformingmars.model.enums.prelude.PreludeEnum;
import it.capozxvii.terraformingmars.model.jpa.Game;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.Points;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PointsServiceTest extends AbstractServiceTest {

    private Player anotherGenericPlayer;
    private Player anotherGenericPlayer2;

    @BeforeAll
    void createTwoGenericPlayers() {

        anotherGenericPlayer = playerRepository.save(
                createPlayer("anotherGenericPlayer", "anotherGenericPlayer"));
        anotherGenericPlayer2 = playerRepository.save(
                createPlayer("anotherGenericPlayer2", "anotherGenericPlayer2"));
    }

    @Test
    void insertPointsTestGameExists() {
        List<PointsDto> pointsDtos = new ArrayList<>();
        pointsDtos.add(createPointsDto(17, 6, 8, 0, 5, 42, ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                       PreludeEnum.INDUSTRIAL_ZONE,
                                       PreludeEnum.BIOLAB,
                                       null,
                                       createPlayerDto("genericPlayer", null,
                                                       genericPlayer.getId())));
        pointsDtos.add(
                createPointsDto(27, 0, 0, 0, 5, 14, ColoniesCorporations.ARKLIGHT,
                                PreludeEnum.LOAN,
                                PreludeEnum.BUSINESS_EMPIRE,
                                null,
                                createPlayerDto("anotherGenericPlayer", null,
                                                anotherGenericPlayer.getId())));
        pointsDtos.add(
                createPointsDto(40, 0, 0, 0, 5, 14, PreludeCorporations.VALLEY_TRUST,
                                PreludeEnum.POWER_GENERATION,
                                PreludeEnum.UNMI_CONTRACTOR,
                                Map.of("aCategory", 20),
                                createPlayerDto("anotherGenericPlayer2", null,
                                                anotherGenericPlayer2.getId())));
        Game game = gameRepository.save(createGame("aLocation", genericChampionship, LocalDateTime.now()));
        assertDoesNotThrow(() -> pointsService.insertPoints(pointsDtos, game.getId(), genericChampionship.getId()));

        Optional<Game> updatedGame = gameRepository.findById(game.getId());
        assertTrue(updatedGame.isPresent());
        assertEquals(3, updatedGame.get().getPoints().size());
        Set<Points> savedPoints = updatedGame.get().getPoints();
        filterAndCheckPoints(savedPoints, ColoniesCorporations.STORMCRAFT_INCORPORATED, 17, 6, 8, 0, 5, 42,
                             PreludeEnum.INDUSTRIAL_ZONE,
                             PreludeEnum.BIOLAB,
                             null,
                             genericPlayer);
        filterAndCheckPoints(savedPoints, ColoniesCorporations.ARKLIGHT, 27, 0, 0, 0, 5, 14,
                             PreludeEnum.LOAN,
                             PreludeEnum.BUSINESS_EMPIRE,
                             null,
                             anotherGenericPlayer);
        filterAndCheckPoints(savedPoints, PreludeCorporations.VALLEY_TRUST, 40, 0, 0, 0, 5, 14,
                             PreludeEnum.POWER_GENERATION,
                             PreludeEnum.UNMI_CONTRACTOR,
                             Map.of("aCategory", 20),
                             anotherGenericPlayer2);
    }

    @Test
    void updatePointsTest() {

        Points points = createPoints(5, 0, 0, 0, 0, 14, null,
                                     ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                     PreludeEnum.INDUSTRIAL_ZONE,
                                     PreludeEnum.BIOLAB,
                                     genericPlayer);

        points = pointsRepository.save(points);

        PointsDto pointsDto = createPointsDto(27, 0, 0, 0, 5, 14,
                                              PreludeCorporations.VALLEY_TRUST,
                                              PreludeEnum.POWER_GENERATION,
                                              PreludeEnum.UNMI_CONTRACTOR,
                                              null,
                                              createPlayerDto("genericPlayer", "genericFullname",
                                                              genericPlayer.getId()));

        pointsDto.setId(points.getId());
        pointsService.updatePoints(pointsDto);
        Optional<Points> res = pointsRepository.findById(points.getId());
        assertTrue(res.isPresent());
        filterAndCheckPoints(Set.of(res.get()), PreludeCorporations.VALLEY_TRUST, 27, 0, 0, 0, 5, 14,
                             PreludeEnum.POWER_GENERATION,
                             PreludeEnum.UNMI_CONTRACTOR, null, genericPlayer);

    }

    @Test
    void pointsOfAGameTest() {
        Points points = createPoints(5, 0, 0, 0, 0, 14, null,
                                     ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                     PreludeEnum.INDUSTRIAL_ZONE,
                                     PreludeEnum.BIOLAB,
                                     genericPlayer);
        points.setGame(genericGame);
        Long yellowPointGameId = pointsRepository.save(points).getId();
        points = createPoints(5, 0, 0, 0, 0, 14, null,
                                     PreludeCorporations.VALLEY_TRUST,
                                     PreludeEnum.POWER_GENERATION,
                                     PreludeEnum.UNMI_CONTRACTOR,
                                     anotherGenericPlayer);
        points.setGame(genericGame);
        Long greenPointGameId = pointsRepository.save(points).getId();

        List<PointsDto> pointsOfGame = pointsService.getPointsOfAGame(genericGame.getId());
        assertFalse(pointsOfGame.isEmpty());
        Optional<PointsDto> yellowPointsDto =
                pointsOfGame.stream().filter(pointsDto -> pointsDto.getId().equals(yellowPointGameId)).findFirst();
        assertTrue(yellowPointsDto.isPresent());
        checkPointsDto(ColoniesCorporations.STORMCRAFT_INCORPORATED, 5, 0, 0, 0, 0, 14,
                       PreludeEnum.INDUSTRIAL_ZONE,
                       PreludeEnum.BIOLAB,
                       null, genericPlayer.getId(),
                       yellowPointsDto.get());

        Optional<PointsDto> greenPointsDto =
                pointsOfGame.stream().filter(pointsDto -> pointsDto.getId().equals(greenPointGameId)).findFirst();
        assertTrue(greenPointsDto.isPresent());
        checkPointsDto(PreludeCorporations.VALLEY_TRUST, 5, 0, 0, 0, 0, 14,
                       PreludeEnum.POWER_GENERATION,
                       PreludeEnum.UNMI_CONTRACTOR,
                       null,
                       anotherGenericPlayer.getId(),
                       greenPointsDto.get());

    }
}
