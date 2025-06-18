package it.capozxvii.terraformingmars.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.capozxvii.terraformingmars.abstracts.AbstractControllerTest;
import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.SimpleWrapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

@WebMvcTest(ChampionshipController.class)
class ChampionshipControllerTest extends AbstractControllerTest {

    @Test
    void insertChampionshipTest() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusYears(1);
        ChampionshipDto championshipDto = ChampionshipDto.builder().name("La casa di clank").startingDate(start)
                .endingDate(end).build();
        when(championshipService.createChampionship(championshipDto)).thenAnswer(im -> {
            championshipDto.setId(1L);
            return championshipDto;
        });
        SimpleWrapper<ChampionshipDto> result = MAPPER.readValue(mvc.perform(
                        post("/championship/insert-championship").contentType(
                                MediaType.APPLICATION_JSON_VALUE).content(MAPPER.writeValueAsString(
                                championshipDto))).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        ChampionshipDto resChampionshipDto = result.getResponseObject();

        assertEquals(1, resChampionshipDto.getId());
        assertEquals("La casa di clank", resChampionshipDto.getName());
        assertEquals(start, resChampionshipDto.getStartingDate());
        assertEquals(end, resChampionshipDto.getEndingDate());
    }

    @Test
    void insertChampionshipExceptionTest() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusYears(1);
        ChampionshipDto championshipDto = ChampionshipDto.builder().name("La casa di clank").startingDate(start)
                .endingDate(end).build();
        TerraformingMarsException exception = new TerraformingMarsException("Unknown error", "");

        when(championshipService.createChampionship(championshipDto)).thenThrow(exception);
        SimpleWrapper<ChampionshipDto> result = MAPPER.readValue(mvc.perform(
                        post("/championship/insert-championship").contentType(
                                MediaType.APPLICATION_JSON_VALUE).content(MAPPER.writeValueAsString(
                                championshipDto))).andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals("Unknown error", result.getMessage());
    }
}
