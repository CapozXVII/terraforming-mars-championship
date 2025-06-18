package it.capozxvii.terraformingmars.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.capozxvii.terraformingmars.abstracts.AbstractControllerTest;
import it.capozxvii.terraformingmars.model.dto.DraftingDto;
import it.capozxvii.terraformingmars.model.enums.corporation.ColoniesCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.CorporateEraCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.PreludeCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.VenusNextCorporations;
import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;
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
        Map<Integer, List<String>> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(2, List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(3, List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        DraftingDto draftingDtoCapoz = createPrevisionDto(1L,
                                                          PlayerID.builder().nickname("Capoz").id(1L).build(),
                                                          corporationDecksToDraft);
        draftingDtoList.add(draftingDtoCapoz);

        corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(2, List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(3, List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION));

        DraftingDto draftingDtoLudovick =
                createPrevisionDto(1L, PlayerID.builder().nickname("Ludovick").id(2L).build(), corporationDecksToDraft);
        draftingDtoList.add(draftingDtoLudovick);

        draftingDtoCapoz.setId(1L);
        draftingDtoLudovick.setId(2L);
        List<DraftingDto> prevRes = List.of(draftingDtoCapoz, draftingDtoLudovick);
        when(previsionService.insertDrafting(draftingDtoList)).thenReturn(prevRes);

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
        Map<Integer, List<String>> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(2, List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(3, List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        DraftingDto draftingDtoCapoz = createPrevisionDto(1L,
                                                          PlayerID.builder().nickname("Capoz").id(1L).build(),
                                                          corporationDecksToDraft);
        draftingDtoList.add(draftingDtoCapoz);
        doThrow(new TerraformingMarsException("Player with id [1L, nickname Capoz] not found")).when(
                previsionService).insertDrafting(draftingDtoList);

        String res =
                MAPPER.readValue(
                        mvc.perform(post("/drafting/insert-draftings").contentType(MediaType.APPLICATION_JSON_VALUE)
                                            .content(MAPPER.writeValueAsString(draftingDtoList)))
                                .andExpect(status().isInternalServerError()).andReturn()
                                .getResponse().getContentAsString(),
                        new TypeReference<CollectionWrapper<DraftingDto>>() {
                        }).getMessage();

        assertEquals("Player with id [1L, nickname Capoz] not found", res);

    }

    @Test
    void insertDraftingChampionshipNotExistingTest() throws Exception {
        List<DraftingDto> draftingDtoList = new ArrayList<>();
        Map<Integer, List<String>> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(2, List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(3, List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        DraftingDto draftingDtoCapoz = createPrevisionDto(100L,
                                                          PlayerID.builder().nickname("Capoz").id(1L).build(),
                                                          corporationDecksToDraft);
        draftingDtoList.add(draftingDtoCapoz);
        doThrow(new TerraformingMarsException("Championship with id 100 not found")).when(
                previsionService).insertDrafting(draftingDtoList);

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
}
