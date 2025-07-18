package it.capozxvii.terraformingmars.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.capozxvii.terraformingmars.abstracts.AbstractServiceTest;
import it.capozxvii.terraformingmars.model.dto.DraftingDto;
import it.capozxvii.terraformingmars.model.enums.corporation.ColoniesCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.CorporateEraCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.PreludeCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.VenusNextCorporations;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Drafting;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DraftingServiceTest extends AbstractServiceTest {

    @Test
    void insertDraftingTest() {
        Player cPlayer = playerRepository.save(createPlayer("cNickname", "fullCName"));
        Player dPlayer = playerRepository.save(createPlayer("dNickname", "fullDName"));
        Championship championship =
                championshipRepository.save(
                        createChampionship("Terraforming Mars I Edition", LocalDateTime.now().truncatedTo(
                                                   ChronoUnit.SECONDS),
                                           LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusYears(1L)));
        List<DraftingDto> previsions = new ArrayList<>();
        Map<Integer, List<String>> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(2, List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(3, List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        previsions.add(createPrevisionDto(championship.getChampionshipId(),
                                          cPlayer.getPlayerId(),
                                          corporationDecksToDraft));
        corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(2, List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(3, List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        previsions.add(createPrevisionDto(championship.getChampionshipId(),
                                          dPlayer.getPlayerId(),
                                          corporationDecksToDraft));
        List<DraftingDto> draftingDtos = assertDoesNotThrow(() -> previsionService.insertDrafting(previsions));
        Optional<DraftingDto> previsionDtoOptional = draftingDtos.stream()
                .filter(previsionDto -> previsionDto.getPlayerId() != null
                                        && previsionDto.getPlayerId().equals(cPlayer.getPlayerId())).findFirst();
        assertTrue(previsionDtoOptional.isPresent());

        DraftingDto draftingDtoResult = previsionDtoOptional.get();
        assertEquals(cPlayer.getPlayerId(), draftingDtoResult.getPlayerId());
        assertEquals(championship.getChampionshipId(), draftingDtoResult.getChampionshipId());
        assertEquals(List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION),
                     draftingDtoResult.getDraftings().get(1));
        assertEquals(List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION),
                     draftingDtoResult.getDraftings().get(2));
        assertEquals(List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION),
                     draftingDtoResult.getDraftings().get(3));

        previsionDtoOptional = draftingDtos.stream()
                .filter(previsionDto -> previsionDto.getPlayerId() != null
                                        && previsionDto.getPlayerId().equals(dPlayer.getPlayerId())).findFirst();
        assertTrue(previsionDtoOptional.isPresent());

        draftingDtoResult = previsionDtoOptional.get();
        assertEquals(dPlayer.getPlayerId(), draftingDtoResult.getPlayerId());
        assertEquals(championship.getChampionshipId(), draftingDtoResult.getChampionshipId());
        assertEquals(List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION),
                     draftingDtoResult.getDraftings().get(1));
        assertEquals(List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION),
                     draftingDtoResult.getDraftings().get(2));
        assertEquals(List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION),
                     draftingDtoResult.getDraftings().get(3));

    }

    @Test
    void insertDraftingTestExceptions() {
        Player ePlayer = playerRepository.save(createPlayer("eNickname", "fullEName"));
        List<DraftingDto> previsions = new ArrayList<>();
        Map<Integer, List<String>> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(2, List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(3, List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        previsions.add(createPrevisionDto(-10L,
                                          -10L,
                                          corporationDecksToDraft));
        TerraformingMarsException exception =
                assertThrows(TerraformingMarsException.class, () -> previsionService.insertDrafting(previsions));

        assertEquals("Player with id [-10] not found", exception.getMessage());
        previsions.clear();
        corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(2, List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(3, List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        previsions.add(createPrevisionDto(-10L,
                                         ePlayer.getPlayerId(),
                                          corporationDecksToDraft));
        exception =
                assertThrows(TerraformingMarsException.class, () -> previsionService.insertDrafting(previsions));
        assertEquals("Championship with id [-10] not found", exception.getMessage());
    }

    @Test
    void editDraftingTests() {
        Player fPlayer = playerRepository.save(createPlayer("fNickname", "fullFName"));
        Championship championship =
                championshipRepository.save(
                        createChampionship("Terraforming Mars IV Edizione", LocalDateTime.now().truncatedTo(
                                                   ChronoUnit.SECONDS),
                                           LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusYears(1L)));
        Map<Integer, List<String>> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(2, List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(3, List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        Drafting drafting = draftingRepository.save(
                Drafting.builder().player(fPlayer).championship(championship).draftings(corporationDecksToDraft)
                        .build());
        List<DraftingDto> draftingDtos = assertDoesNotThrow(() -> previsionService.editDrafting(
                DraftingDto.builder().championshipId(championship.getChampionshipId()).id(drafting.getDraftingId())
                        .playerId(
                               fPlayer.getPlayerId())
                        .draftings(Map.of(2, List.of(ColoniesCorporations.EXPANSION, VenusNextCorporations.EXPANSION)))
                        .build()));
        Optional<DraftingDto> dto = draftingDtos.stream().findFirst();
        assertTrue(dto.isPresent());

        Map<Integer, List<String>> predictionAfterEdit = dto.get().getDraftings();
        assertEquals(3, predictionAfterEdit.size());
        assertEquals(List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION),
                     predictionAfterEdit.get(1));
        assertEquals(List.of(ColoniesCorporations.EXPANSION, VenusNextCorporations.EXPANSION),
                     predictionAfterEdit.get(2));
        assertEquals(List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION),
                     predictionAfterEdit.get(3));
    }
}
