package it.capozxvii.terraformingmars.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.capozxvii.terraformingmars.abstracts.AbstractServiceTest;
import it.capozxvii.terraformingmars.model.dto.GameDto;
import it.capozxvii.terraformingmars.model.dto.PointsDto;
import it.capozxvii.terraformingmars.model.enums.corporation.ColoniesCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.PreludeCorporations;
import it.capozxvii.terraformingmars.model.enums.prelude.PreludeEnum;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Game;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.Points;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
class GameServiceTest extends AbstractServiceTest {

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
    void insertGameTest() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

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
        GameDto gameDto = createGameDto(now, "The house", genericChampionship.getId());
        gameDto.setPoints(pointsDtos);

        GameDto savedGameDto = assertDoesNotThrow(
                () -> gameService.insertGame(gameDto));
        assertNotNull(savedGameDto.getId());
        assertTrue(savedGameDto.getId() > 0);
        assertEquals(genericChampionship.getId(), savedGameDto.getChampionshipId());
        assertEquals(now, savedGameDto.getGameDate());
        assertEquals("The house", savedGameDto.getLocation());
        Optional<Game> gameEntity = gameRepository.findById(savedGameDto.getId());
        assertTrue(gameEntity.isPresent());
        assertEquals(3, gameEntity.get().getPoints().size());
        Set<Points> savedPoints = gameEntity.get().getPoints();
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
    void editGameTest() {
        Game game = gameRepository.save(
                createGame("house edi", genericChampionship, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));

        LocalDateTime updateGameDateTime = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);
        GameDto gameDto = createGameDto(updateGameDateTime, "The house", genericChampionship.getId());
        gameDto.setId(game.getId());
        GameDto edited = assertDoesNotThrow(() -> gameService.editGame(gameDto));
        assertEquals(game.getId(), edited.getId());
        assertEquals(updateGameDateTime, edited.getGameDate());
        assertEquals("The house", edited.getLocation());
        assertEquals(genericChampionship.getId(), edited.getChampionshipId());
    }

    @Test
    void findByLocationTest() {
        Game game1 = gameRepository.save(
                createGame("House 2", genericChampionship, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
        Game game2 = gameRepository.save(
                createGame("House 2", genericChampionship, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
        Game game3 = gameRepository.save(
                createGame("House 2", genericChampionship, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
        Game game4 = gameRepository.save(
                createGame("House 2", genericChampionship, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
        gameRepository.save(
                createGame("House 3", genericChampionship, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
        gameRepository.save(
                createGame("House 4", genericChampionship, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
        List<GameDto> gameDtos = gameService.findByLocation("House 2");
        assertEquals(4, gameDtos.size());
        filterAndCheckGameDtos(gameDtos, game1);
        filterAndCheckGameDtos(gameDtos, game2);
        filterAndCheckGameDtos(gameDtos, game3);
        filterAndCheckGameDtos(gameDtos, game4);
    }

    @Test
    void findByChampionshipIdTest() {

        Championship champ =
                championshipRepository.save(createChampionship("Championship,", LocalDateTime.of(2000, 1, 1, 1, 1),
                                                               LocalDateTime.of(2020, 1, 1, 1, 1)));

        Points points1 = createPoints(17, 6, 8, 0, 5, 42, null, ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                      PreludeEnum.INDUSTRIAL_ZONE,
                                      PreludeEnum.BIOLAB,
                                      genericPlayer);
        Game game1 = createGame("House of championship", champ,
                                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        game1 = gameRepository.save(game1);
        points1.setGame(game1);
        pointsRepository.save(points1);

        Points points2 = createPoints(50, 6, 8, 0, 5, 42, null, ColoniesCorporations.POSEIDON,
                                      PreludeEnum.METAL_RICH_ASTEROID,
                                      PreludeEnum.BIOLAB,
                                      anotherGenericPlayer);
        Game game2 = createGame("House of championship2", champ,
                                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        game2 = gameRepository.save(game2);
        points2.setGame(game2);
        pointsRepository.save(points2);

        Points points3 = createPoints(500, 6, 8, 0, 5, 42, null, ColoniesCorporations.ARKLIGHT,
                                      PreludeEnum.METAL_RICH_ASTEROID,
                                      PreludeEnum.BIOLAB,
                                      anotherGenericPlayer2);
        Game game3 = createGame("House of championship3", champ,
                                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        game3 = gameRepository.save(game3);
        points3.setGame(game3);
        pointsRepository.save(points3);

        List<GameDto> res = gameService.findByChampionshipId(champ.getId());

        assertEquals(3, res.size());
        filterAndCheckGameDtos(res, game1);
        filterAndCheckGameDtos(res, game2);
        filterAndCheckGameDtos(res, game3);
        checkPointsDto(ColoniesCorporations.STORMCRAFT_INCORPORATED, 17, 6, 8, 0, 5, 42,
                       PreludeEnum.INDUSTRIAL_ZONE,
                       PreludeEnum.BIOLAB,
                       null,
                       genericPlayer.getId(), res.getFirst().getPoints().getFirst());
        checkPointsDto(ColoniesCorporations.POSEIDON, 50, 6, 8, 0, 5, 42,
                       PreludeEnum.METAL_RICH_ASTEROID,
                       PreludeEnum.BIOLAB,
                       null,
                       anotherGenericPlayer.getId(), res.get(1).getPoints().getFirst());
        checkPointsDto(ColoniesCorporations.ARKLIGHT, 500, 6, 8, 0, 5, 42,
                       PreludeEnum.METAL_RICH_ASTEROID,
                       PreludeEnum.BIOLAB,
                       null,
                       anotherGenericPlayer2.getId(), res.get(2).getPoints().getFirst());

    }
}
