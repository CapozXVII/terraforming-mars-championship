package it.capozxvii.terraformingmars.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.capozxvii.terraformingmars.abstracts.AbstractServiceTest;
import it.capozxvii.terraformingmars.model.dto.GameDto;
import it.capozxvii.terraformingmars.model.jpa.Game;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameServiceTest extends AbstractServiceTest {

    @Test
    void insertGameTest() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        GameDto gameDto = assertDoesNotThrow(
                () -> gameService.insertGame(createGameDto(now, "The house", genericChampionship.getId())));

        assertNotNull(gameDto.getId());
        assertTrue(gameDto.getId() > 0);
        assertEquals(genericChampionship.getId(), gameDto.getChampionshipId());
        assertEquals(now, gameDto.getGameDate());
        assertEquals("The house", gameDto.getLocation());
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
}
