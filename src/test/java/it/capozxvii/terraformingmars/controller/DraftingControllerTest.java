package it.capozxvii.terraformingmars.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.capozxvii.terraformingmars.abstracts.AbstractControllerTest;
import it.capozxvii.terraformingmars.model.dto.CorporationsExpansionPairDto;
import it.capozxvii.terraformingmars.model.dto.DraftingDto;
import it.capozxvii.terraformingmars.model.enums.corporation.ColoniesCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.CorporateEraCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.PreludeCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.VenusNextCorporations;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.CollectionWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

@WebMvcTest(DraftingController.class)
class DraftingControllerTest extends AbstractControllerTest {


    @Test
    void insertDraftingTest() throws Exception {
        List<DraftingDto> draftingDtoList = new ArrayList<>();
        Map<Long, CorporationsExpansionPairDto> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1L, CorporationsExpansionPairDto.builder()
                .firstExpansion(VenusNextCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(2L, CorporationsExpansionPairDto.builder()
                .firstExpansion(CorporateEraCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(3L, CorporationsExpansionPairDto.builder()
                .firstExpansion(ColoniesCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        DraftingDto draftingDtoCapoz = createDraftingDto(1L,
                                                         createPlayerDto("aNickname", "aFullname", 1L),
                                                         corporationDecksToDraft);
        draftingDtoList.add(draftingDtoCapoz);

        corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1L, CorporationsExpansionPairDto.builder()
                .firstExpansion(VenusNextCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(2L, CorporationsExpansionPairDto.builder()
                .firstExpansion(CorporateEraCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(3L, CorporationsExpansionPairDto.builder()
                .firstExpansion(ColoniesCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());

        DraftingDto draftingDtoLudovick =
                createDraftingDto(1L, createPlayerDto("bNickname", "bFullname", 2L),
                                  corporationDecksToDraft);
        draftingDtoList.add(draftingDtoLudovick);

        draftingDtoCapoz.setId(1L);
        draftingDtoLudovick.setId(2L);
        List<DraftingDto> prevRes = List.of(draftingDtoCapoz, draftingDtoLudovick);
        when(draftinService.insertDrafting(draftingDtoList)).thenReturn(prevRes);

        List<DraftingDto> res =
                MAPPER.readValue(
                        mvc.perform(post("/drafting/insert-draftings").contentType(MediaType.APPLICATION_JSON_VALUE)
                                            .content(MAPPER.writeValueAsString(draftingDtoList)))
                                .andExpect(status().isOk()).andReturn()
                                .getResponse().getContentAsString(),
                        new TypeReference<CollectionWrapper<DraftingDto>>() {
                        }).getResponseObject();

        assertEquals(prevRes, res);
    }

    @Test
    void insertDraftingPlayerNotExistingTest() throws Exception {
        List<DraftingDto> draftingDtoList = new ArrayList<>();
        Map<Long, CorporationsExpansionPairDto> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1L, CorporationsExpansionPairDto.builder()
                .firstExpansion(VenusNextCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(2L, CorporationsExpansionPairDto.builder()
                .firstExpansion(CorporateEraCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(3L, CorporationsExpansionPairDto.builder()
                .firstExpansion(ColoniesCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        DraftingDto draftingDtoCapoz = createDraftingDto(1L,
                                                         createPlayerDto("aNickname", "aFullname", 1L),
                                                         corporationDecksToDraft);
        draftingDtoList.add(draftingDtoCapoz);
        doThrow(new TerraformingMarsException("Player with id [1L] not found")).when(
                draftinService).insertDrafting(draftingDtoList);

        String res =
                MAPPER.readValue(
                        mvc.perform(post("/drafting/insert-draftings").contentType(MediaType.APPLICATION_JSON_VALUE)
                                            .content(MAPPER.writeValueAsString(draftingDtoList)))
                                .andExpect(status().isInternalServerError()).andReturn()
                                .getResponse().getContentAsString(),
                        new TypeReference<CollectionWrapper<DraftingDto>>() {
                        }).getMessage();

        assertEquals("Player with id [1L] not found", res);

    }

    @Test
    void insertDraftingChampionshipNotExistingTest() throws Exception {
        List<DraftingDto> draftingDtoList = new ArrayList<>();
        Map<Long, CorporationsExpansionPairDto> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1L, CorporationsExpansionPairDto.builder()
                .firstExpansion(VenusNextCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(2L, CorporationsExpansionPairDto.builder()
                .firstExpansion(CorporateEraCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(3L, CorporationsExpansionPairDto.builder()
                .firstExpansion(ColoniesCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        DraftingDto draftingDtoCapoz = createDraftingDto(100L,
                                                         createPlayerDto("aNickname", "aFullname", 1L),
                                                         corporationDecksToDraft);
        draftingDtoList.add(draftingDtoCapoz);
        doThrow(new TerraformingMarsException("Championship with id 100 not found")).when(
                draftinService).insertDrafting(draftingDtoList);

        String res =
                MAPPER.readValue(
                        mvc.perform(post("/drafting/insert-draftings").contentType(MediaType.APPLICATION_JSON_VALUE)
                                            .content(MAPPER.writeValueAsString(draftingDtoList)))
                                .andExpect(status().isInternalServerError()).andReturn()
                                .getResponse().getContentAsString(),
                        new TypeReference<CollectionWrapper<DraftingDto>>() {
                        }).getMessage();

        assertEquals("Championship with id 100 not found", res);

    }

    @Test
    void viewDraftingsTest() throws Exception {

        List<DraftingDto> draftingDtoList = new ArrayList<>();
        Map<Long, CorporationsExpansionPairDto> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1L, CorporationsExpansionPairDto.builder()
                .firstExpansion(VenusNextCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(2L, CorporationsExpansionPairDto.builder()
                .firstExpansion(CorporateEraCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(3L, CorporationsExpansionPairDto.builder()
                .firstExpansion(ColoniesCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());

        draftingDtoList.add(
                createDraftingDto(1L, createPlayerDto("aNickname", "aFullname", 1L), corporationDecksToDraft));

        corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1L, CorporationsExpansionPairDto.builder()
                .firstExpansion(VenusNextCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(2L, CorporationsExpansionPairDto.builder()
                .firstExpansion(CorporateEraCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(3L, CorporationsExpansionPairDto.builder()
                .firstExpansion(ColoniesCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        draftingDtoList.add(
                createDraftingDto(1L, createPlayerDto("bNickname", "bFullname", 2L), corporationDecksToDraft));

        corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1L, CorporationsExpansionPairDto.builder()
                .firstExpansion(VenusNextCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(2L, CorporationsExpansionPairDto.builder()
                .firstExpansion(CorporateEraCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(3L, CorporationsExpansionPairDto.builder()
                .firstExpansion(ColoniesCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        draftingDtoList.add(
                createDraftingDto(1L, createPlayerDto("cNickname", "cFullname", 3L), corporationDecksToDraft));

        when(draftinService.viewDraftings(1L)).thenReturn(draftingDtoList);


        List<DraftingDto> res =
                MAPPER.readValue(
                        mvc.perform(get("/drafting/view-draftings").contentType(MediaType.APPLICATION_JSON_VALUE)
                                            .queryParam("championshipId", "1"))
                                .andExpect(status().isOk()).andReturn()
                                .getResponse().getContentAsString(),
                        new TypeReference<CollectionWrapper<DraftingDto>>() {
                        }).getResponseObject();

        assertEquals(draftingDtoList, res);
    }
}
