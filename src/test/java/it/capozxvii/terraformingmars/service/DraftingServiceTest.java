package it.capozxvii.terraformingmars.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.capozxvii.terraformingmars.abstracts.AbstractServiceTest;
import it.capozxvii.terraformingmars.model.dto.CorporationsExpansionPairDto;
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
        previsions.add(createDraftingDto(championship.getChampionshipId(),
                                         createPlayerDto("", "", cPlayer.getPlayerId()),
                                         corporationDecksToDraft));

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
        previsions.add(createDraftingDto(championship.getChampionshipId(),
                                         createPlayerDto("", "", dPlayer.getPlayerId()),
                                         corporationDecksToDraft));
        List<DraftingDto> draftingDtos = assertDoesNotThrow(() -> draftingService.insertDrafting(previsions));
        Optional<DraftingDto> previsionDtoOptional = draftingDtos.stream()
                .filter(previsionDto -> previsionDto.getPlayer() != null
                                        && previsionDto.getPlayer().getId().equals(cPlayer.getPlayerId())).findFirst();
        assertTrue(previsionDtoOptional.isPresent());

        DraftingDto draftingDtoResult = previsionDtoOptional.get();
        assertEquals(cPlayer.getPlayerId(), draftingDtoResult.getPlayer().getId());
        assertEquals(championship.getChampionshipId(), draftingDtoResult.getChampionshipId());
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(VenusNextCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION)
                             .build(), draftingDtoResult.getDraftings().get(1));
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(CorporateEraCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION)
                             .build(), draftingDtoResult.getDraftings().get(2));
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(ColoniesCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION)
                             .build(), draftingDtoResult.getDraftings().get(3));

        previsionDtoOptional = draftingDtos.stream()
                .filter(previsionDto -> previsionDto.getPlayer() != null
                                        && previsionDto.getPlayer().getId().equals(dPlayer.getPlayerId())).findFirst();
        assertTrue(previsionDtoOptional.isPresent());

        draftingDtoResult = previsionDtoOptional.get();
        assertEquals(dPlayer.getPlayerId(), draftingDtoResult.getPlayer().getId());
        assertEquals(championship.getChampionshipId(), draftingDtoResult.getChampionshipId());
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(VenusNextCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION)
                             .build(), draftingDtoResult.getDraftings().get(1));
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(CorporateEraCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION)
                             .build(), draftingDtoResult.getDraftings().get(2));
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(ColoniesCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION)
                             .build(), draftingDtoResult.getDraftings().get(3));

    }

    @Test
    void insertDraftingTestExceptions() {
        Player ePlayer = playerRepository.save(createPlayer("eNickname", "fullEName"));
        List<DraftingDto> previsions = new ArrayList<>();
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
        previsions.add(createDraftingDto(-10L,
                                         createPlayerDto("", "", -10L),
                                         corporationDecksToDraft));
        TerraformingMarsException exception =
                assertThrows(TerraformingMarsException.class, () -> draftingService.insertDrafting(previsions));

        assertEquals("Player with id [-10] not found", exception.getMessage());
        previsions.clear();
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
        previsions.add(createDraftingDto(-10L,
                                         createPlayerDto("", "", ePlayer.getPlayerId()),
                                         corporationDecksToDraft));
        exception =
                assertThrows(TerraformingMarsException.class, () -> draftingService.insertDrafting(previsions));
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
        Drafting drafting = draftingRepository.save(
                Drafting.builder().player(fPlayer).championship(championship).draftings(corporationDecksToDraft)
                        .build());
        List<DraftingDto> draftingDtos = assertDoesNotThrow(() -> draftingService.editDrafting(
                DraftingDto.builder().championshipId(championship.getChampionshipId()).id(drafting.getDraftingId())
                        .player(
                                createPlayerDto("", "", fPlayer.getPlayerId()))
                        .draftings(Map.of(2L, CorporationsExpansionPairDto.builder()
                                .firstExpansion(ColoniesCorporations.EXPANSION)
                                .secondExpansion(VenusNextCorporations.EXPANSION).build()))
                        .build()));
        Optional<DraftingDto> dto = draftingDtos.stream().findFirst();
        assertTrue(dto.isPresent());

        Map<Long, CorporationsExpansionPairDto> predictionAfterEdit = dto.get().getDraftings();
        assertEquals(3, predictionAfterEdit.size());
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(VenusNextCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION).build(),
                     predictionAfterEdit.get(1));
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(ColoniesCorporations.EXPANSION)
                             .secondExpansion(VenusNextCorporations.EXPANSION).build(),
                     predictionAfterEdit.get(2));
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(ColoniesCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION).build(),
                     predictionAfterEdit.get(3));
    }

    @Test
    void viewDraftingsTest() {

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
        Drafting drafting = createDrafting(genericPlayer, corporationDecksToDraft, genericChampionship);
        draftingRepository.save(drafting);
        List<DraftingDto> res = draftingService.viewDraftings(genericChampionship.getChampionshipId());

        assertEquals(1, res.size());
        assertEquals(genericPlayer.getPlayerId(), res.getFirst().getPlayer().getId());
        assertEquals(genericChampionship.getChampionshipId(), res.getFirst().getChampionshipId());
        Map<Long, CorporationsExpansionPairDto> resDrafts = res.getFirst().getDraftings();
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(VenusNextCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION).build(),
                     resDrafts.get(1L));
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(CorporateEraCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION).build(),
                     resDrafts.get(2L));
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(ColoniesCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION).build(),
                     resDrafts.get(3L));
        
    }
}
