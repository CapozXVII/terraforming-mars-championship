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
import org.junit.jupiter.api.Test;

class PlayerServiceTest extends AbstractServiceTest {

    @Test
    void insertPlayerTest() {

        String nickname = "aNickname";
        String fullName = "FullAName";

        PlayerDto playerDto =
                playerService.insertPlayer(PlayerDto.builder().nickname(nickname).fullname(fullName).build());

        assertEquals(nickname, playerDto.getNickname());
        assertEquals(fullName, playerDto.getFullname());
        assertNotNull(playerDto.getId());
        assertTrue(playerDto.getId() > 0);
    }

    @Test
    void getPlayerById() {
        String nickname = "bNickname";
        String fullName = "FullBName";
        Player player = playerRepository.save(Player.builder().nickname(nickname).fullname(fullName).build());

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
}
