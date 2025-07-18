package it.capozxvii.terraformingmars.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.capozxvii.terraformingmars.abstracts.AbstractServiceTest;
import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class ChampionshipServiceTest extends AbstractServiceTest {

    @Test
    void insertChampionshipTest() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusYears(1L);
        String name = "La casa di clank";

        ChampionshipDto championshipDto = championshipService.createChampionship(
                ChampionshipDto.builder().name(name).startingDate(now)
                        .endingDate(end).build());

        assertEquals(now, championshipDto.getStartingDate());
        assertEquals(end, championshipDto.getEndingDate());
        assertEquals(name, championshipDto.getName());
        assertNotNull(championshipDto.getId());
        assertTrue(championshipDto.getId() > 0);
    }

    @Test
    void getChampionshipByNameTest() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusYears(1L);
        String name = "La casa di clank II Edizione";
        Championship championship =
                championshipRepository.save(
                        Championship.builder().endingDate(end).startingDate(now).name(name).build());
        ChampionshipDto championshipDto = assertDoesNotThrow(() -> championshipService.getChampionshipByName(name));
        assertEquals(end, championshipDto.getEndingDate());
        assertEquals(now, championshipDto.getStartingDate());
        assertEquals(name, championshipDto.getName());
        assertEquals(championship.getChampionshipId(), championshipDto.getId());
    }

    @Test
    void getChampionshipByNameExceptionTest() {

        TerraformingMarsException res = assertThrows(TerraformingMarsException.class,
                                                     () -> championshipService.getChampionshipByName("notExisting"));

        assertEquals("Championship with id [notExisting] not found", res.getMessage());
    }

    @Test
    void getChampionshipByIdTest() {
        ChampionshipDto championshipDto =
                championshipService.getChampionshipById(genericChampionship.getChampionshipId());
        assertEquals(genericChampionship.getChampionshipId(), championshipDto.getId());
        assertEquals(genericChampionship.getName(), championshipDto.getName());
        assertEquals(genericChampionship.getStartingDate().truncatedTo(ChronoUnit.SECONDS),
                     championshipDto.getStartingDate().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(genericChampionship.getEndingDate().truncatedTo(ChronoUnit.SECONDS),
                     championshipDto.getEndingDate().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void getChampionshipByIdExceptionTest() {
        TerraformingMarsException terraformingMarsException =
                assertThrows(TerraformingMarsException.class, () -> championshipService.getChampionshipById(-100L));
        assertEquals("Championship with id [-100] not found", terraformingMarsException.getMessage());
    }
}
