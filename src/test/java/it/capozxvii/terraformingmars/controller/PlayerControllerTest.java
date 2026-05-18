package it.capozxvii.terraformingmars.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.capozxvii.terraformingmars.abstracts.AbstractControllerTest;
import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.CollectionWrapper;
import it.capozxvii.terraformingmars.util.wrapper.SimpleWrapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest extends AbstractControllerTest {

    @Test
    void insertPlayerTest() throws Exception {
        PlayerDto playerDto = createPlayerDto("capoz", "Cri Cap", null);
        when(playerService.insertPlayer(playerDto)).thenAnswer(im -> {
            playerDto.setId(1L);
            return playerDto;
        });
        String res = MAPPER.readValue(
                mvc.perform(post("/player/insert-player").contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .content(MAPPER.writeValueAsString(playerDto))).andExpect(status().isCreated())
                        .andReturn()
                        .getResponse().getContentAsString(),
                new TypeReference<SimpleWrapper<PlayerDto>>() {
                }).getMessage();
        assertEquals("Player with id [capoz] has been saved", res);
    }

    @Test
    void insertPlayerExceptionTest() throws Exception {
        PlayerDto playerDto = createPlayerDto("capoz", "Cri Cap", null);
        TerraformingMarsException exception = new TerraformingMarsException("Unknown error", "");
        when(playerService.insertPlayer(playerDto)).thenThrow(exception);
        String res = MAPPER.readValue(
                mvc.perform(post("/player/insert-player").contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .content(MAPPER.writeValueAsString(playerDto)))
                        .andExpect(status().isInternalServerError())
                        .andReturn()
                        .getResponse().getContentAsString(),
                new TypeReference<SimpleWrapper<PlayerDto>>() {
                }).getMessage();
        assertEquals("Unknown error", res);
    }

    @Test
    void updatePlayerTest() throws Exception {
        PlayerDto playerDto = createPlayerDto("capoz", "Cri Cap", 1L);
        when(playerService.updatePlayer(playerDto)).thenReturn(playerDto);
        String res = MAPPER.readValue(
                mvc.perform(put("/player/update-player").contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .content(MAPPER.writeValueAsString(playerDto))).andExpect(status().isOk())
                        .andReturn()
                        .getResponse().getContentAsString(),
                new TypeReference<SimpleWrapper<PlayerDto>>() {
                }).getMessage();
        assertEquals("Player with id [capoz] has been updated", res);
    }

    @Test
    void updatePlayerExceptionTest() throws Exception {
        PlayerDto playerDto = createPlayerDto("capoz", "Cri Cap", 1L);
        TerraformingMarsException exception = new TerraformingMarsException("Unknown error", "");
        when(playerService.updatePlayer(playerDto)).thenThrow(exception);
        String res = MAPPER.readValue(
                mvc.perform(put("/player/update-player").contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .content(MAPPER.writeValueAsString(playerDto)))
                        .andExpect(status().isInternalServerError())
                        .andReturn()
                        .getResponse().getContentAsString(),
                new TypeReference<SimpleWrapper<PlayerDto>>() {
                }).getMessage();
        assertEquals("Unknown error", res);
    }

    @Test
    void getPlayerTest() throws Exception {
        when(playerService.getPlayerById(1L)).thenReturn(createPlayerDto("capoz", "Cri Cap", 1L));
        PlayerDto res = MAPPER.readValue(mvc.perform(get("/player").contentType(MediaType.APPLICATION_JSON_VALUE)
                                                             .content(MAPPER.writeValueAsString(1L)))
                                                 .andExpect(status().isOk()).andReturn()
                                                 .getResponse().getContentAsString(),
                                         new TypeReference<SimpleWrapper<PlayerDto>>() {
                                         }).getResponseObject();

        assertEquals("capoz", res.getNickname());
        assertEquals(1L, res.getId());
        assertEquals("Cri Cap", res.getFullname());
    }

    @Test
    void getPlayerExceptionTest() throws Exception {
        TerraformingMarsException exception =
                new TerraformingMarsException("Player with id [1L] not found", "");
        when(playerService.getPlayerById(1L)).thenThrow(exception);
        String res = MAPPER.readValue(mvc.perform(get("/player").contentType(MediaType.APPLICATION_JSON_VALUE)
                                                          .content(MAPPER.writeValueAsString(1L)))
                                              .andExpect(status().isInternalServerError()).andReturn()
                                              .getResponse().getContentAsString(),
                                      new TypeReference<SimpleWrapper<PlayerDto>>() {
                                      }).getMessage();
        assertEquals("Player with id [1L] not found", res);
    }

    @Test
    void deletePlayerTest() throws Exception {
        doNothing().when(playerService).deletePlayer(1L);
        String res = MAPPER.readValue(
                mvc.perform(delete("/player/delete-player").contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .content(MAPPER.writeValueAsString(1L))).andExpect(status().isOk())
                        .andReturn()
                        .getResponse().getContentAsString(),
                new TypeReference<SimpleWrapper<PlayerDto>>() {
                }).getMessage();
        assertEquals("Player with id [1] has been deleted", res);
    }

    @Test
    void deletePlayerExceptionTest() throws Exception {
        TerraformingMarsException exception = new TerraformingMarsException("Unknown error", "");
        doThrow(exception).when(playerService).deletePlayer(1L);
        String res = MAPPER.readValue(
                mvc.perform(delete("/player/delete-player").contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .content(MAPPER.writeValueAsString(1L)))
                        .andExpect(status().isInternalServerError())
                        .andReturn()
                        .getResponse().getContentAsString(),
                new TypeReference<SimpleWrapper<PlayerDto>>() {
                }).getMessage();
        assertEquals("Unknown error", res);
    }

    @Test
    void retrieveAllPlayersTest() throws Exception {
        List<PlayerDto> players = new ArrayList<>();
        players.add(createPlayerDto("capoz", "CriCap", 1L));
        players.add(createPlayerDto("zopac", "CapCri", 2L));
        players.add(createPlayerDto("zopac", "CapCri", 2L));
        when(playerService.getAllPlayers()).thenReturn(players);
        List<PlayerDto> res =
                MAPPER.readValue(mvc.perform(get("/player/all-players").contentType(MediaType.APPLICATION_JSON_VALUE))
                                         .andExpect(status().isOk()).andReturn()
                                         .getResponse().getContentAsString(),
                                 new TypeReference<CollectionWrapper<PlayerDto>>() {
                                 }).getResponseObject();
        assertEquals(players, res);
    }
}
