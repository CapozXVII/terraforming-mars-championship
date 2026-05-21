package it.capozxvii.terraformingmars.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.capozxvii.terraformingmars.abstracts.AbstractServiceTest;
import it.capozxvii.terraformingmars.model.enums.corporation.ColoniesCorporations;
import it.capozxvii.terraformingmars.model.enums.prelude.PreludeEnum;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Game;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.Points;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ReportServiceTest extends AbstractServiceTest {

    @Test
    void createChampionshipReportTest() {
        LocalDateTime gameDate = LocalDateTime.of(2025, 1, 2, 3, 4);
        String suffix = String.valueOf(System.nanoTime());
        String championshipName = "Report Championship " + suffix;
        String playerNickname = "reportPlayer" + suffix;
        Championship championship = championshipRepository.save(createChampionship(
                championshipName, gameDate.minusDays(1), gameDate.plusDays(1)));
        Player player = playerRepository.save(createPlayer(playerNickname, "Report Player"));
        Game game = gameRepository.save(createGame("Report Room", championship, gameDate));
        Points points = createPoints(17, 6, 8, 0, 5, 42, Map.of("Heat", 2),
                                     ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                     PreludeEnum.EXCENTRIC_SPONSOR,
                                     PreludeEnum.BIOLAB,
                                     player);
        points.setGame(game);
        pointsRepository.save(points);

        byte[] report = reportService.createChampionshipReport(championship.getChampionshipId());
        String pdf = new String(report, StandardCharsets.ISO_8859_1);

        assertTrue(pdf.startsWith("%PDF-1.4"));
        assertTrue(pdf.contains("Championship: " + championshipName));
        assertTrue(pdf.contains("Current standing"));
        assertTrue(pdf.contains(playerNickname));
        assertTrue(pdf.contains("Game 1: Report Room - 2025-01-02 03:04"));
        assertTrue(pdf.contains("TR"));
        assertTrue(pdf.contains("Heat=2"));
        assertTrue(pdf.contains("80"));
    }

    @Test
    void createChampionshipReportNotFoundTest() {
        TerraformingMarsException exception = assertThrows(
                TerraformingMarsException.class, () -> reportService.createChampionshipReport(-999999L));

        assertEquals("Championship with id [-999999] not found", exception.getMessage());
    }
}
