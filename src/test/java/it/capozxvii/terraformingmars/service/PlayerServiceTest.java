package it.capozxvii.terraformingmars.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.capozxvii.terraformingmars.abstracts.AbstractServiceTest;
import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerServiceTest extends AbstractServiceTest {

    @Test
    void insertPlayerTest() {

        String nickname = "aNickname";
        String fullName = "FullAName";

        PlayerDto playerDto =
                playerService.insertPlayer(createPlayerDto(nickname, fullName, null));

        assertEquals(nickname, playerDto.getNickname());
        assertEquals(fullName, playerDto.getFullname());
        assertNotNull(playerDto.getId());
        assertTrue(playerDto.getId() > 0);
    }

    @Test
    void getPlayerById() {
        String nickname = "bNickname";
        String fullName = "FullBName";
        Player player = playerRepository.save(createPlayer(nickname, fullName));

        PlayerDto playerDto =
                playerService.getPlayerById(PlayerID.builder().nickname(nickname).id(player.getId()).build());

        assertEquals(nickname, playerDto.getNickname());
        assertEquals(fullName, playerDto.getFullname());
        assertNotNull(playerDto.getId());
        assertEquals(player.getId(), playerDto.getId());
    }

    @Test
    void getPlayerByIdNotExisting() {
        TerraformingMarsException res = assertThrows(TerraformingMarsException.class,
                () -> playerService.getPlayerById(
                        PlayerID.builder().nickname("notExisting").build()));

        assertEquals("Player with id [null, nickname notExisting] not found", res.getMessage());
    }

    @Test
    void editPlayerTest() {
        String nickname = "editNickname";
        String fullName = "FullBName";
        Player player = playerRepository.save(Player.builder().nickname(nickname).fullname(fullName).build());
        PlayerDto playerDto = playerService.updatePlayer(
                PlayerDto.builder().nickname(nickname).fullname("newFullname").id(player.getId()).build());
        assertEquals(nickname, playerDto.getNickname());
        assertEquals(player.getId(), playerDto.getId());
        assertEquals("newFullname", playerDto.getFullname());
    }

    @Test
    void deletePlayerTest() {
        String nickname = "toDeleteNickname";
        String fullName = "FullBName";
        Player player = playerRepository.save(Player.builder().nickname(nickname).fullname(fullName).build());
        playerService.deletePlayer(PlayerID.builder().nickname(nickname).id(player.getId()).build());

        assertTrue(
                playerRepository.findById(PlayerID.builder().nickname(nickname).id(player.getId()).build()).isEmpty());
    }
    
    @Test
    void getAllPlayersTest() {
        playerRepository.save(createPlayer("allPlayer1Nickname", "allPlayer1Fullname"));
        playerRepository.save(createPlayer("allPlayer2Nickname", "allPlayer2Fullname"));
        playerRepository.save(createPlayer("allPlayer3Nickname", "allPlayer3Fullname"));
        List<PlayerDto> res = playerService.getAllPlayers();
        assertTrue(res.stream().anyMatch(playerDto -> playerDto.getNickname().equals("allPlayer1Nickname")
                                                      && playerDto.getFullname().equals("allPlayer1Fullname")));
        assertTrue(res.stream().anyMatch(playerDto -> playerDto.getNickname().equals("allPlayer2Nickname")
                                                      && playerDto.getFullname().equals("allPlayer2Fullname")));
        assertTrue(res.stream().anyMatch(playerDto -> playerDto.getNickname().equals("allPlayer3Nickname")
                                                      && playerDto.getFullname().equals("allPlayer3Fullname")));
    }
}
