package it.capozxvii.terraformingmars.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.capozxvii.terraformingmars.abstracts.AbstractControllerTest;
import it.capozxvii.terraformingmars.model.dto.PointsDto;
import it.capozxvii.terraformingmars.model.enums.corporation.ColoniesCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.PreludeCorporations;
import it.capozxvii.terraformingmars.model.enums.prelude.PreludeEnum;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.CollectionWrapper;
import it.capozxvii.terraformingmars.util.wrapper.SimpleWrapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

@WebMvcTest(PointsController.class)
class PointsControllerTest extends AbstractControllerTest {

    @Test
    void insertPointsTest() throws Exception {
        List<PointsDto> pointsDtoList = new ArrayList<>();
        pointsDtoList.add(createPointsDto(17, 6, 8, 0, 5, 42, ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                          PreludeEnum.INDUSTRIAL_ZONE,
                                          PreludeEnum.BIOLAB,
                                          null,
                                          createPlayerDto("Capoz", "Cri Cap", null)));
        pointsDtoList.add(createPointsDto(27, 0, 0, 0, 5, 14, ColoniesCorporations.ARKLIGHT,
                                          PreludeEnum.LOAN,
                                          PreludeEnum.BUSINESS_EMPIRE,
                                          null,
                                          createPlayerDto("Ludovick", "Lud", null)));
        pointsDtoList.add(createPointsDto(40, 0, 0, 0, 5, 14, PreludeCorporations.VALLEY_TRUST,
                                          PreludeEnum.POWER_GENERATION,
                                          PreludeEnum.UNMI_CONTRACTOR,
                                          null,
                                          createPlayerDto("Ganj", "Ivan", null)));
        Long gameId = 1L;
        Long championshipId = 1L;

        doNothing().when(pointsService).insertPoints(pointsDtoList, gameId, championshipId);

        String res = mvc.perform(post("/points/insert-points").contentType(MediaType.APPLICATION_JSON_VALUE)
                                         .content(MAPPER.writeValueAsString(pointsDtoList))
                                         .param("gameId", String.valueOf(gameId))
                                         .param("championshipId", String.valueOf(championshipId)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        verify(pointsService, times(1)).insertPoints(pointsDtoList, gameId, championshipId);

        assertEquals("Points have been saved", res);
    }

    @Test
    void insertPointsChampionshipNotExisting() throws Exception {

        List<PointsDto> pointsDtoList = new ArrayList<>();
        pointsDtoList.add(createPointsDto(17, 6, 8, 0, 5, 42, ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                          PreludeEnum.INDUSTRIAL_ZONE,
                                          PreludeEnum.BIOLAB,
                                          null,
                                          createPlayerDto("Capoz", "Cri Cap", null)));
        pointsDtoList.add(createPointsDto(27, 0, 0, 0, 5, 14, ColoniesCorporations.ARKLIGHT,
                                          PreludeEnum.LOAN,
                                          PreludeEnum.BUSINESS_EMPIRE,
                                          null,
                                          createPlayerDto("Ludovick", "Lud", null)));
        pointsDtoList.add(createPointsDto(40, 0, 0, 0, 5, 14, PreludeCorporations.VALLEY_TRUST,
                                          PreludeEnum.POWER_GENERATION,
                                          PreludeEnum.UNMI_CONTRACTOR,
                                          null,
                                          createPlayerDto("Ganj", "Ivan", null)));
        Long gameId = 1L;
        Long championshipId = 15454554L;

        doThrow(new TerraformingMarsException("Championship with id 15454554 not found")).when(pointsService)
                .insertPoints(pointsDtoList, gameId, championshipId);

        String res = mvc.perform(post("/points/insert-points").contentType(MediaType.APPLICATION_JSON_VALUE)
                                         .content(MAPPER.writeValueAsString(pointsDtoList))
                                         .param("gameId", String.valueOf(gameId))
                                         .param("championshipId", String.valueOf(championshipId)))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString();
        verify(pointsService, times(1)).insertPoints(pointsDtoList, gameId, championshipId);

        assertEquals("Championship with id 15454554 not found", res);
    }

    @Test
    void insertPointsPlayerNotExistingTest() throws Exception {
        List<PointsDto> pointsDtoList = new ArrayList<>();
        pointsDtoList.add(createPointsDto(17, 6, 8, 0, 5, 42, ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                          PreludeEnum.INDUSTRIAL_ZONE,
                                          PreludeEnum.BIOLAB,
                                          null,
                                          createPlayerDto("Capoz", "Cri Cap", null)));
        pointsDtoList.add(createPointsDto(27, 0, 0, 0, 5, 14, ColoniesCorporations.ARKLIGHT,
                                          PreludeEnum.LOAN,
                                          PreludeEnum.BUSINESS_EMPIRE,
                                          null,
                                          createPlayerDto("Ludovick", "Lud", null)));
        pointsDtoList.add(createPointsDto(40, 0, 0, 0, 5, 14, PreludeCorporations.VALLEY_TRUST,
                                          PreludeEnum.POWER_GENERATION,
                                          PreludeEnum.UNMI_CONTRACTOR,
                                          null,
                                          createPlayerDto("Ganj", "Ivan", null)));

        Long gameId = 1L;
        Long championshipId = 1L;

        doThrow(new TerraformingMarsException("Player with id 17, nickname Capoz not found")).when(pointsService)
                .insertPoints(pointsDtoList, gameId, championshipId);

        String res = mvc.perform(post("/points/insert-points").contentType(MediaType.APPLICATION_JSON_VALUE)
                                         .content(MAPPER.writeValueAsString(pointsDtoList))
                                         .param("gameId", String.valueOf(gameId))
                                         .param("championshipId", String.valueOf(championshipId)))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse().getContentAsString();
        verify(pointsService, times(1)).insertPoints(pointsDtoList, gameId, championshipId);

        assertEquals("Player with id 17, nickname Capoz not found", res);
    }

    @Test
    void updatePointsTest() throws Exception {
        PointsDto updating = createPointsDto(17, 6, 8, 0, 5, 42, ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                             PreludeEnum.INDUSTRIAL_ZONE,
                                             PreludeEnum.BIOLAB,
                                             null,
                                             createPlayerDto("Capoz", "Cri Cap", null));
        updating.setId(10L);

        when(pointsService.updatePoints(updating)).thenReturn(updating);

        PointsDto res =
                MAPPER.readValue(mvc.perform(put("/points/update-points").contentType(MediaType.APPLICATION_JSON_VALUE)
                                                     .content(MAPPER.writeValueAsString(updating)))
                                         .andExpect(status().isOk()).andReturn()
                                         .getResponse().getContentAsString(),
                                 new TypeReference<SimpleWrapper<PointsDto>>() {
                                 }).getResponseObject();

        assertEquals(updating, res);
    }

    @Test
    void getPointsOfAGameTest() throws Exception {
        List<PointsDto> pointsDtoList = new ArrayList<>();
        pointsDtoList.add(createPointsDto(17, 6, 8, 0, 5, 42, ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                          PreludeEnum.INDUSTRIAL_ZONE,
                                          PreludeEnum.BIOLAB,
                                          null,
                                          createPlayerDto("Capoz", "Cri Cap", null)));
        pointsDtoList.add(createPointsDto(27, 0, 0, 0, 5, 14, ColoniesCorporations.ARKLIGHT,
                                          PreludeEnum.LOAN,
                                          PreludeEnum.BUSINESS_EMPIRE,
                                          null,
                                          createPlayerDto("Ludovick", "Lud", null)));
        pointsDtoList.add(createPointsDto(40, 0, 0, 0, 5, 14, PreludeCorporations.VALLEY_TRUST,
                                          PreludeEnum.POWER_GENERATION,
                                          PreludeEnum.UNMI_CONTRACTOR,
                                          null,
                                          createPlayerDto("Ganj", "Ivan", null)));

        Long gameId = 1L;

        when(pointsService.getPointsOfAGame(gameId)).thenReturn(pointsDtoList);

        List<PointsDto> res =
                MAPPER.readValue(mvc.perform(get("/points/points-of-game").contentType(MediaType.APPLICATION_JSON_VALUE)
                                                     .param("gameId", gameId.toString()))
                                         .andExpect(status().isOk()).andReturn()
                                         .getResponse().getContentAsString(),
                                 new TypeReference<CollectionWrapper<PointsDto>>() {
                                 }).getResponseObject();

        assertEquals(pointsDtoList, res);
    }

    @Test
    void getPointsOfAGameExceptionTest() throws Exception {
        Long gameId = -1L;

        doThrow(new TerraformingMarsException("Game with id [-1] not found")).when(
                pointsService).getPointsOfAGame(gameId);


        String res =
                MAPPER.readValue(mvc.perform(get("/points/points-of-game").contentType(MediaType.APPLICATION_JSON_VALUE)
                                                     .param("gameId", gameId.toString()))
                                         .andExpect(status().isInternalServerError()).andReturn()
                                         .getResponse().getContentAsString(),
                                 new TypeReference<CollectionWrapper<PointsDto>>() {
                                 }).getMessage();

        assertEquals("Game with id [-1] not found", res);
    }

}
