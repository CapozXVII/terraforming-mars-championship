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
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ReportServiceTest extends AbstractServiceTest {

    @Test
    void createChampionshipReportTest() {
        LocalDateTime gameDate = LocalDateTime.of(2025, 1, 2, 3, 4);
        String suffix = String.valueOf(System.nanoTime());
        String championshipName = "Report Championship " + suffix;
        String firstPlayerNickname = "reportPlayerA" + suffix;
        String secondPlayerNickname = "reportPlayerB" + suffix;
        Championship championship = championshipRepository.save(createChampionship(
                championshipName, gameDate.minusDays(1), gameDate.plusDays(1)));
        Player firstPlayer = playerRepository.save(createPlayer(firstPlayerNickname, "Report Player A"));
        Player secondPlayer = playerRepository.save(createPlayer(secondPlayerNickname, "Report Player B"));
        Game firstGame = gameRepository.save(createGame("Report Room", championship, gameDate));
        Game secondGame = gameRepository.save(createGame("Second Room", championship, gameDate.plusDays(1)));
        Points firstGameFirstPlayerPoints = createPoints(17, 6, 8, 0, 5, 42, Map.of("Heat", 2),
                                                         ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                                         PreludeEnum.EXCENTRIC_SPONSOR,
                                                         PreludeEnum.BIOLAB,
                                                         firstPlayer);
        firstGameFirstPlayerPoints.setGame(firstGame);
        pointsRepository.save(firstGameFirstPlayerPoints);
        Points firstGameSecondPlayerPoints = createPoints(10, 4, 6, 0, 0, 10, null,
                                                          ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                                          PreludeEnum.EXCENTRIC_SPONSOR,
                                                          PreludeEnum.BIOLAB,
                                                          secondPlayer);
        firstGameSecondPlayerPoints.setGame(firstGame);
        pointsRepository.save(firstGameSecondPlayerPoints);
        Points secondGameFirstPlayerPoints = createPoints(10, 5, 5, 5, 0, 45, null,
                                                          ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                                          PreludeEnum.EXCENTRIC_SPONSOR,
                                                          PreludeEnum.BIOLAB,
                                                          firstPlayer);
        secondGameFirstPlayerPoints.setGame(secondGame);
        pointsRepository.save(secondGameFirstPlayerPoints);
        Points secondGameSecondPlayerPoints = createPoints(12, 5, 5, 5, 5, 28, null,
                                                           ColoniesCorporations.STORMCRAFT_INCORPORATED,
                                                           PreludeEnum.EXCENTRIC_SPONSOR,
                                                           PreludeEnum.BIOLAB,
                                                           secondPlayer);
        secondGameSecondPlayerPoints.setGame(secondGame);
        pointsRepository.save(secondGameSecondPlayerPoints);

        byte[] report = reportService.createChampionshipReport(championship.getChampionshipId());
        String pdf = new String(report, StandardCharsets.ISO_8859_1);

        assertTrue(pdf.startsWith("%PDF-1.4"));
        assertTrue(pdf.contains("Championship: " + championshipName));
        assertTrue(pdf.contains("Current standing"));
        assertTrue(pdf.contains("Gap to First"));
        assertTrue(pdf.contains("Gap to Trailing"));
        assertTrue(pdf.contains(firstPlayerNickname));
        assertTrue(pdf.contains(secondPlayerNickname));
        assertTrue(pdf.contains(String.format(Locale.ROOT, "%-30s %10d %14d %16d", firstPlayerNickname, 150, 0, 0)));
        assertTrue(pdf.contains(
                String.format(Locale.ROOT, "%-30s %10d %14d %16d", secondPlayerNickname, 90, -60, -60)));
        assertTrue(pdf.contains("Game 1: Report Room - 2025-01-02 03:04"));
        assertTrue(pdf.contains("Game 2: Second Room - 2025-01-03 03:04"));
        assertTrue(pdf.contains("TR"));
        assertTrue(pdf.contains("Heat=2"));
        assertTrue(pdf.contains("80"));
        assertTrue(pdf.contains("Global total points trend"));
        assertTrue(pdf.contains("Cumulative championship points"));
        assertTrue(pdf.contains("Terraforming rating"));
        assertTrue(pdf.contains("Greeneries"));
        assertTrue(pdf.contains("Cities"));
        assertTrue(pdf.contains("Cards"));
        assertTrue(pdf.contains("Awards"));
        assertTrue(pdf.contains("Milestones"));
        assertTrue(pdf.contains("Total points"));
        assertTrue(pdf.contains("G1: 2025-01-02 03:04 | Report Room"));
        assertTrue(pdf.contains("G2: 2025-01-03 03:04 | Second Room"));
        assertTrue(pdf.contains("X axis: game date and location"));
        assertTrue(pdf.contains("1.00 0.00 0.10 RG"));
        assertTrue(pdf.contains("0.00 0.42 1.00 RG"));
    }

    @Test
    void createChampionshipReportNotFoundTest() {
        TerraformingMarsException exception = assertThrows(
                TerraformingMarsException.class, () -> reportService.createChampionshipReport(-999999L));

        assertEquals("Championship with id [-999999] not found", exception.getMessage());
    }

    @Test
    void createChampionshipReportFileWithManyGamesAndPlayersTest() throws Exception {
        LocalDateTime firstGameDate = LocalDateTime.of(2025, 3, 1, 20, 0);
        String suffix = Long.toString(System.nanoTime(), 36);
        String championshipName = "Large Report Championship " + suffix;
        Championship championship = championshipRepository.save(createChampionship(
                championshipName, firstGameDate.minusDays(1), firstGameDate.plusMonths(1)));
        List<Player> players = createReportPlayers(suffix);

        for (int gameNumber = 1; gameNumber <= 10; gameNumber++) {
            Game game = gameRepository.save(createGame(String.format(Locale.ROOT, "Chart Room %02d", gameNumber),
                                                       championship,
                                                       firstGameDate.plusDays(gameNumber - 1L)));
            for (int playerIndex = 0; playerIndex < players.size(); playerIndex++) {
                Points points = createReportPoints(gameNumber, playerIndex, players.get(playerIndex));
                points.setGame(game);
                pointsRepository.save(points);
            }
        }

        byte[] report = reportService.createChampionshipReport(championship.getChampionshipId());
        Path reportPath = Path.of("target", "championship-report-large-test-" + suffix + ".pdf");
        Files.createDirectories(reportPath.getParent());
        Files.write(reportPath, report);

        String pdf = new String(report, StandardCharsets.ISO_8859_1);
        assertTrue(Files.exists(reportPath));
        assertTrue(Files.size(reportPath) > 0);
        assertTrue(pdf.startsWith("%PDF-1.4"));
        assertTrue(pdf.contains("Championship: " + championshipName));
        assertTrue(pdf.contains("Gap to First"));
        assertTrue(pdf.contains("Gap to Trailing"));
        assertTrue(pdf.contains("Global total points trend"));
        assertTrue(pdf.contains("Terraforming rating"));
        assertTrue(pdf.contains("Total points"));
        assertTrue(pdf.contains("X axis: game date and location"));
        assertTrue(pdf.contains("G10: 2025-03-10 20:00 | Chart Room 10"));
        assertTrue(pdf.contains("Game 10: Chart Room 10 - 2025-03-10 20:00"));
        assertTrue(pdf.contains("Other categories: Heat=14, Venus=4"));
        players.forEach(player -> assertTrue(pdf.contains(player.getNickname())));
    }

    private List<Player> createReportPlayers(final String suffix) {
        List<Player> players = new ArrayList<>();
        for (int playerNumber = 1; playerNumber <= 5; playerNumber++) {
            players.add(playerRepository.save(createPlayer("chartP" + playerNumber + suffix,
                                                           "Chart Player " + playerNumber)));
        }
        return players;
    }

    private Points createReportPoints(final int gameNumber, final int playerIndex, final Player player) {
        return createPoints(
                10 + gameNumber + playerIndex,
                (gameNumber + playerIndex) % 7,
                (gameNumber * 2 + playerIndex) % 8,
                gameNumber % 2 == 0 ? 5 : 0,
                playerIndex % 2 == 0 ? 5 : 0,
                20 + playerIndex * 3 + (10 - gameNumber),
                Map.of("Heat", gameNumber + playerIndex, "Venus", playerIndex),
                ColoniesCorporations.STORMCRAFT_INCORPORATED,
                PreludeEnum.EXCENTRIC_SPONSOR,
                PreludeEnum.BIOLAB,
                player);
    }
}
