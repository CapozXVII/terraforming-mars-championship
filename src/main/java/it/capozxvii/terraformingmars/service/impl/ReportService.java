package it.capozxvii.terraformingmars.service.impl;

import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;
import it.capozxvii.terraformingmars.model.dto.GameDto;
import it.capozxvii.terraformingmars.model.dto.PointsDto;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.mapper.ChampionshipMapper;
import it.capozxvii.terraformingmars.repository.ChampionshipRepository;
import it.capozxvii.terraformingmars.service.IReportService;
import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService implements IReportService {

    private static final int MAX_LINES_PER_PAGE = 64;
    private static final int MAX_LINE_LENGTH = 112;
    private static final int PAGE_HEIGHT = 842;
    private static final int LEFT_MARGIN = 36;
    private static final int FIRST_LINE_Y = 806;
    private static final int LINE_HEIGHT = 12;
    private static final int CHART_LEFT = 62;
    private static final int CHART_BOTTOM = 300;
    private static final int CHART_WIDTH = 460;
    private static final int CHART_HEIGHT = 385;
    private static final DateTimeFormatter GAME_DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm", Locale.ROOT);
    private static final PdfColor[] CHART_COLORS = {
            new PdfColor(1.00, 0.00, 0.10),
            new PdfColor(0.00, 0.42, 1.00),
            new PdfColor(0.00, 0.75, 0.20),
            new PdfColor(1.00, 0.55, 0.00),
            new PdfColor(0.65, 0.20, 1.00),
            new PdfColor(0.00, 0.85, 0.95),
            new PdfColor(1.00, 0.00, 0.65),
            new PdfColor(0.95, 0.80, 0.00)
    };

    private final ChampionshipRepository championshipRepository;
    private final ChampionshipMapper championshipMapper;

    public ReportService(final ChampionshipRepository championshipRepository,
            final ChampionshipMapper championshipMapper) {
        this.championshipRepository = championshipRepository;
        this.championshipMapper = championshipMapper;
    }

    @Override
    @Transactional
    public byte[] createChampionshipReport(final Long championshipId) {
        Championship championship = championshipRepository.findById(championshipId).orElseThrow(
                () -> new TerraformingMarsException(Message.NOT_FOUND, Championship.class.getSimpleName(),
                                                    championshipId));
        return createPdf(buildReport(championshipMapper.toDto(championship)));
    }

    private ReportDocument buildReport(final ChampionshipDto championship) {
        return new ReportDocument(buildReportLines(championship), buildCharts(championship));
    }

    private List<String> buildReportLines(final ChampionshipDto championship) {
        List<String> lines = new ArrayList<>();
        addLine(lines, "Championship report");
        addLine(lines, "Championship: " + text(championship.getName()));
        addLine(lines, "");
        addStanding(lines, championship);
        addLine(lines, "");
        addGames(lines, championship);
        return lines;
    }

    private void addStanding(final List<String> lines, final ChampionshipDto championship) {
        addLine(lines, "Current standing");
        addLine(lines, String.format(Locale.ROOT, "%-30s %10s %14s %16s", "Player", "Points",
                                     "Gap to First", "Gap to Trailing"));
        addLine(lines, String.format(Locale.ROOT, "%-30s %10s %14s %16s", "------------------------------",
                                     "----------", "--------------", "----------------"));

        Map<String, Long> standing = championship.getStanding() != null
                                     ? championship.getStanding().getPlayersPoints()
                                     : Map.of();
        if (standing == null || standing.isEmpty()) {
            addLine(lines, "No standing available");
            return;
        }
        List<Map.Entry<String, Long>> standingRows = standing.entrySet().stream().sorted(this::compareStandingRows)
                .toList();
        long leaderPoints = standingRows.getFirst().getValue();
        Long previousPoints = null;
        for (Map.Entry<String, Long> entry : standingRows) {
            long gapToFirst = entry.getValue() - leaderPoints;
            long gapToTrailing = previousPoints == null ? 0 : entry.getValue() - previousPoints;
            addLine(lines, String.format(Locale.ROOT, "%-30s %10d %14d %16d", fit(entry.getKey(), 30),
                                         entry.getValue(), gapToFirst, gapToTrailing));
            previousPoints = entry.getValue();
        }
    }

    private int compareStandingRows(final Map.Entry<String, Long> first, final Map.Entry<String, Long> second) {
        int pointsComparison = Long.compare(second.getValue(), first.getValue());
        if (pointsComparison != 0) {
            return pointsComparison;
        }
        return first.getKey().compareTo(second.getKey());
    }

    private List<Chart> buildCharts(final ChampionshipDto championship) {
        List<GameDto> games = sortedGames(championship);
        List<String> players = sortedPlayers(championship, games);
        if (games.isEmpty() || players.isEmpty()) {
            return List.of();
        }

        List<String> xLabels = xLabels(games);
        List<Chart> charts = new ArrayList<>();
        charts.add(globalTotalPointsChart(games, players, xLabels));
        charts.add(categoryChart("Terraforming rating", games, players, xLabels, PointsDto::getTerraformingRating));
        charts.add(categoryChart("Greeneries", games, players, xLabels, PointsDto::getGreenery));
        charts.add(categoryChart("Cities", games, players, xLabels, PointsDto::getCity));
        charts.add(categoryChart("Cards", games, players, xLabels, PointsDto::getCards));
        charts.add(categoryChart("Awards", games, players, xLabels, PointsDto::getAwards));
        charts.add(categoryChart("Milestones", games, players, xLabels, PointsDto::getMilestones));
        charts.add(categoryChart("Total points", games, players, xLabels, this::totalPoints));
        return charts;
    }

    private List<GameDto> sortedGames(final ChampionshipDto championship) {
        return championship.getGames() == null
               ? List.of()
               : championship.getGames().stream().sorted(gameComparator()).toList();
    }

    private List<String> sortedPlayers(final ChampionshipDto championship, final List<GameDto> games) {
        Map<String, Long> standing = championship.getStanding() != null
                                     ? championship.getStanding().getPlayersPoints()
                                     : Map.of();
        List<String> players = new ArrayList<>();
        if (standing != null) {
            standing.entrySet().stream().sorted(this::compareStandingRows)
                    .map(Map.Entry::getKey)
                    .forEach(players::add);
        }
        games.stream()
                .flatMap(game -> game.getPoints() == null ? List.<PointsDto>of().stream() : game.getPoints().stream())
                .map(this::playerName)
                .filter(player -> !players.contains(player))
                .sorted()
                .forEach(players::add);
        return players;
    }

    private List<String> xLabels(final List<GameDto> games) {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            labels.add("G" + (i + 1) + ": " + formattedDate(games.get(i)) + " | " + text(games.get(i).getLocation()));
        }
        return labels;
    }

    private Chart globalTotalPointsChart(final List<GameDto> games, final List<String> players,
            final List<String> xLabels) {
        Map<String, Long> cumulativePoints = new LinkedHashMap<>();
        Map<String, List<Long>> values = emptyValues(players);
        players.forEach(player -> cumulativePoints.put(player, 0L));

        games.forEach(game -> {
            Map<String, Long> gamePoints = gamePoints(game, this::totalPoints);
            players.forEach(player -> {
                long currentTotal = cumulativePoints.get(player) + gamePoints.getOrDefault(player, 0L);
                cumulativePoints.put(player, currentTotal);
                values.get(player).add(currentTotal);
            });
        });
        return new Chart("Global total points trend", "Cumulative championship points", xLabels,
                         chartSeries(values));
    }

    private Chart categoryChart(final String title, final List<GameDto> games, final List<String> players,
            final List<String> xLabels, final ToIntFunction<PointsDto> extractor) {
        Map<String, List<Long>> values = emptyValues(players);
        games.forEach(game -> {
            Map<String, Long> gamePoints = gamePoints(game, extractor);
            players.forEach(player -> values.get(player).add(gamePoints.getOrDefault(player, 0L)));
        });
        return new Chart(title, "Points scored in each game", xLabels, chartSeries(values));
    }

    private Map<String, List<Long>> emptyValues(final List<String> players) {
        Map<String, List<Long>> values = new LinkedHashMap<>();
        players.forEach(player -> values.put(player, new ArrayList<>()));
        return values;
    }

    private Map<String, Long> gamePoints(final GameDto game, final ToIntFunction<PointsDto> extractor) {
        if (game.getPoints() == null) {
            return Map.of();
        }
        return game.getPoints().stream().collect(Collectors.groupingBy(
                this::playerName, TreeMap::new, Collectors.summingLong(extractor::applyAsInt)));
    }

    private List<ChartSeries> chartSeries(final Map<String, List<Long>> values) {
        List<ChartSeries> series = new ArrayList<>();
        int colorIndex = 0;
        for (Map.Entry<String, List<Long>> entry : values.entrySet()) {
            series.add(new ChartSeries(entry.getKey(), CHART_COLORS[colorIndex % CHART_COLORS.length],
                                       entry.getValue()));
            colorIndex++;
        }
        return series;
    }

    private void addGames(final List<String> lines, final ChampionshipDto championship) {
        addLine(lines, "Games");
        List<GameDto> games = sortedGames(championship);
        if (games.isEmpty()) {
            addLine(lines, "No games played");
            return;
        }
        for (int i = 0; i < games.size(); i++) {
            addLine(lines, "");
            addGame(lines, games.get(i), i + 1);
        }
    }

    private Comparator<GameDto> gameComparator() {
        return Comparator.comparing(GameDto::getGameDate, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(GameDto::getId, Comparator.nullsLast(Comparator.naturalOrder()));
    }

    private void addGame(final List<String> lines, final GameDto game, final int gameNumber) {
        addLine(lines, "Game " + gameNumber + ": " + text(game.getLocation()) + " - " + formattedDate(game));
        addLine(lines, String.format(Locale.ROOT, "%-18s %4s %7s %4s %10s %6s %5s %5s %5s",
                                     "Player", "TR", "Green", "City", "Milestones", "Awards", "Cards",
                                     "Other", "Total"));
        addLine(lines, String.format(Locale.ROOT, "%-18s %4s %7s %4s %10s %6s %5s %5s %5s",
                                     "------------------", "----", "-------", "----", "----------", "------",
                                     "-----", "-----", "-----"));

        List<PointsDto> points = game.getPoints() == null
                                 ? List.of()
                                 : game.getPoints().stream().sorted(pointsComparator()).toList();
        if (points.isEmpty()) {
            addLine(lines, "No points available");
            return;
        }
        points.forEach(pointsDto -> {
            addLine(lines, String.format(Locale.ROOT, "%-18s %4d %7d %4d %10d %6d %5d %5d %5d",
                                         fit(playerName(pointsDto), 18),
                                         pointsDto.getTerraformingRating(),
                                         pointsDto.getGreenery(),
                                         pointsDto.getCity(),
                                         pointsDto.getMilestones(),
                                         pointsDto.getAwards(),
                                         pointsDto.getCards(),
                                         otherCategoriesTotal(pointsDto),
                                         totalPoints(pointsDto)));
            if (pointsDto.getOtherCategories() != null && !pointsDto.getOtherCategories().isEmpty()) {
                addLine(lines, "  Other categories: " + otherCategories(pointsDto));
            }
        });
    }

    private Comparator<PointsDto> pointsComparator() {
        return Comparator.comparing(this::totalPoints).reversed();
    }

    private String playerName(final PointsDto pointsDto) {
        return pointsDto.getPlayer() == null ? "" : text(pointsDto.getPlayer().getNickname());
    }

    private int otherCategoriesTotal(final PointsDto pointsDto) {
        return pointsDto.getOtherCategories() == null
               ? 0
               : pointsDto.getOtherCategories().values().stream().mapToInt(Integer::intValue).sum();
    }

    private String otherCategories(final PointsDto pointsDto) {
        return new TreeMap<>(pointsDto.getOtherCategories()).entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", "));
    }

    private int totalPoints(final PointsDto pointsDto) {
        return pointsDto.getTerraformingRating()
               + pointsDto.getGreenery()
               + pointsDto.getCity()
               + pointsDto.getMilestones()
               + pointsDto.getAwards()
               + pointsDto.getCards()
               + otherCategoriesTotal(pointsDto);
    }

    private String formattedDate(final GameDto game) {
        return game.getGameDate() == null ? "No date" : GAME_DATE_FORMATTER.format(game.getGameDate());
    }

    private void addLine(final List<String> lines, final String line) {
        if (line.length() <= MAX_LINE_LENGTH) {
            lines.add(line);
            return;
        }
        int start = 0;
        while (start < line.length()) {
            int end = Math.min(start + MAX_LINE_LENGTH, line.length());
            lines.add(line.substring(start, end));
            start = end;
        }
    }

    private byte[] createPdf(final ReportDocument reportDocument) {
        List<String> pageContentStreams = new ArrayList<>();
        splitPages(reportDocument.lines()).stream().map(this::contentStream).forEach(pageContentStreams::add);
        reportDocument.charts().stream().map(this::chartContentStream).forEach(pageContentStreams::add);

        StringBuilder pdf = new StringBuilder("%PDF-1.4\n");
        List<Integer> offsets = new ArrayList<>();
        int totalObjects = 3 + pageContentStreams.size() * 2;

        appendObject(pdf, offsets, 1, "<< /Type /Catalog /Pages 2 0 R >>");
        appendObject(pdf, offsets, 2, pagesObject(pageContentStreams.size()));
        appendObject(pdf, offsets, 3, "<< /Type /Font /Subtype /Type1 /BaseFont /Courier >>");

        for (int pageIndex = 0; pageIndex < pageContentStreams.size(); pageIndex++) {
            int pageObjectId = 4 + pageIndex * 2;
            int contentObjectId = pageObjectId + 1;
            appendObject(pdf, offsets, pageObjectId, pageObject(contentObjectId));
            appendObject(pdf, offsets, contentObjectId, contentObject(pageContentStreams.get(pageIndex)));
        }

        int xrefStart = pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length;
        pdf.append("xref\n0 ").append(totalObjects + 1).append("\n");
        pdf.append("0000000000 65535 f \n");
        offsets.forEach(offset -> pdf.append(String.format(Locale.ROOT, "%010d 00000 n %n", offset)));
        pdf.append("trailer\n<< /Size ").append(totalObjects + 1).append(" /Root 1 0 R >>\n");
        pdf.append("startxref\n").append(xrefStart).append("\n%%EOF");
        return pdf.toString().getBytes(StandardCharsets.ISO_8859_1);
    }

    private List<List<String>> splitPages(final List<String> lines) {
        List<List<String>> pages = new ArrayList<>();
        for (int start = 0; start < lines.size(); start += MAX_LINES_PER_PAGE) {
            pages.add(lines.subList(start, Math.min(start + MAX_LINES_PER_PAGE, lines.size())));
        }
        if (pages.isEmpty()) {
            pages.add(List.of(""));
        }
        return pages;
    }

    private String pagesObject(final int pageCount) {
        String kids = "";
        for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
            kids = kids + (4 + pageIndex * 2) + " 0 R ";
        }
        return "<< /Type /Pages /Kids [" + kids + "] /Count " + pageCount + " >>";
    }

    private String pageObject(final int contentObjectId) {
        return "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 " + PAGE_HEIGHT
               + "] /Resources << /Font << /F1 3 0 R >> >> /Contents " + contentObjectId + " 0 R >>";
    }

    private String contentObject(final String content) {
        int length = content.getBytes(StandardCharsets.ISO_8859_1).length;
        return "<< /Length " + length + " >>\nstream\n" + content + "\nendstream";
    }

    private String contentStream(final List<String> lines) {
        StringBuilder content = new StringBuilder("BT\n/F1 9 Tf\n");
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            int y = FIRST_LINE_Y - lineIndex * LINE_HEIGHT;
            content.append("1 0 0 1 ").append(LEFT_MARGIN).append(" ").append(y).append(" Tm\n");
            content.append("(").append(escapePdf(lines.get(lineIndex))).append(") Tj\n");
        }
        content.append("ET");
        return content.toString();
    }

    private String chartContentStream(final Chart chart) {
        StringBuilder content = new StringBuilder();
        appendText(content, chart.title(), 14, LEFT_MARGIN, FIRST_LINE_Y);
        appendText(content, chart.subtitle(), 10, LEFT_MARGIN, FIRST_LINE_Y - 18);
        appendAxes(content, chart);
        appendSeries(content, chart);
        appendXAxisLabels(content, chart);
        appendLegend(content, chart);
        return content.toString();
    }

    private void appendAxes(final StringBuilder content, final Chart chart) {
        long maxValue = maxChartValue(chart);
        appendStrokeColor(content, new PdfColor(0.00, 0.00, 0.00));
        content.append("1 w\n");
        appendLine(content, CHART_LEFT, CHART_BOTTOM, CHART_LEFT, CHART_BOTTOM + CHART_HEIGHT);
        appendLine(content, CHART_LEFT, CHART_BOTTOM, CHART_LEFT + CHART_WIDTH, CHART_BOTTOM);

        appendStrokeColor(content, new PdfColor(0.85, 0.85, 0.85));
        for (int i = 1; i <= 4; i++) {
            double y = CHART_BOTTOM + (CHART_HEIGHT / 4.0) * i;
            appendLine(content, CHART_LEFT, y, CHART_LEFT + CHART_WIDTH, y);
        }

        appendFillColor(content, new PdfColor(0.00, 0.00, 0.00));
        for (int i = 0; i <= 4; i++) {
            long value = Math.round(maxValue * (i / 4.0));
            double y = CHART_BOTTOM + (CHART_HEIGHT / 4.0) * i - 3;
            appendText(content, String.valueOf(value), 8, 28, y);
        }
        appendText(content, "X axis: game date and location", 9, CHART_LEFT, CHART_BOTTOM - 22);
    }

    private void appendSeries(final StringBuilder content, final Chart chart) {
        long maxValue = maxChartValue(chart);
        for (ChartSeries series : chart.series()) {
            appendStrokeColor(content, series.color());
            appendFillColor(content, series.color());
            content.append("1.8 w\n");
            for (int i = 1; i < series.values().size(); i++) {
                appendLine(content, xCoordinate(i - 1, series.values().size()),
                           yCoordinate(series.values().get(i - 1), maxValue),
                           xCoordinate(i, series.values().size()),
                           yCoordinate(series.values().get(i), maxValue));
            }
            for (int i = 0; i < series.values().size(); i++) {
                appendRectangle(content, xCoordinate(i, series.values().size()) - 2,
                                yCoordinate(series.values().get(i), maxValue) - 2, 4, 4);
            }
            if (!series.values().isEmpty()) {
                long lastValue = series.values().getLast();
                appendText(content, String.valueOf(lastValue), 8,
                           CHART_LEFT + CHART_WIDTH + 6,
                           yCoordinate(lastValue, maxValue) - 3);
            }
        }
    }

    private void appendXAxisLabels(final StringBuilder content, final Chart chart) {
        appendFillColor(content, new PdfColor(0.00, 0.00, 0.00));
        for (int i = 0; i < chart.xLabels().size(); i++) {
            String gameKey = "G" + (i + 1);
            appendText(content, gameKey, 8, xCoordinate(i, chart.xLabels().size()) - 6, CHART_BOTTOM - 12);
        }
        for (int i = 0; i < chart.xLabels().size(); i++) {
            double x = i % 2 == 0 ? CHART_LEFT : CHART_LEFT + 240;
            double y = CHART_BOTTOM - 42 - (i / 2) * 11.0;
            appendText(content, fit(chart.xLabels().get(i), 44), 8, x, y);
        }
    }

    private void appendLegend(final StringBuilder content, final Chart chart) {
        double legendX = 370;
        double legendY = 786;
        for (int i = 0; i < chart.series().size(); i++) {
            ChartSeries series = chart.series().get(i);
            double y = legendY - i * 12.0;
            appendFillColor(content, series.color());
            appendRectangle(content, legendX, y - 3, 8, 8);
            appendFillColor(content, new PdfColor(0.00, 0.00, 0.00));
            appendText(content, fit(series.name(), 24), 8, legendX + 12, y);
        }
    }

    private long maxChartValue(final Chart chart) {
        return Math.max(1, chart.series().stream()
                .flatMap(series -> series.values().stream())
                .mapToLong(Long::longValue)
                .max()
                .orElse(1));
    }

    private double xCoordinate(final int index, final int size) {
        if (size <= 1) {
            return CHART_LEFT + CHART_WIDTH / 2.0;
        }
        return CHART_LEFT + (CHART_WIDTH / (double) (size - 1)) * index;
    }

    private double yCoordinate(final long value, final long maxValue) {
        return CHART_BOTTOM + (CHART_HEIGHT * (value / (double) maxValue));
    }

    private void appendLine(final StringBuilder content, final double startX, final double startY, final double endX,
            final double endY) {
        content.append(String.format(Locale.ROOT, "%.2f %.2f m %.2f %.2f l S%n", startX, startY, endX, endY));
    }

    private void appendRectangle(final StringBuilder content, final double x, final double y, final double width,
            final double height) {
        content.append(String.format(Locale.ROOT, "%.2f %.2f %.2f %.2f re f%n", x, y, width, height));
    }

    private void appendStrokeColor(final StringBuilder content, final PdfColor color) {
        content.append(String.format(Locale.ROOT, "%.2f %.2f %.2f RG%n", color.red(), color.green(), color.blue()));
    }

    private void appendFillColor(final StringBuilder content, final PdfColor color) {
        content.append(String.format(Locale.ROOT, "%.2f %.2f %.2f rg%n", color.red(), color.green(), color.blue()));
    }

    private void appendText(final StringBuilder content, final String value, final int fontSize, final double x,
            final double y) {
        content.append(String.format(Locale.ROOT, "BT%n/F1 %d Tf%n1 0 0 1 %.2f %.2f Tm%n(%s) Tj%nET%n",
                                     fontSize, x, y, escapePdf(value)));
    }

    private void appendObject(final StringBuilder pdf, final List<Integer> offsets, final int id,
            final String object) {
        offsets.add(pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length);
        pdf.append(id).append(" 0 obj\n").append(object).append("\nendobj\n");
    }

    private String escapePdf(final String value) {
        return text(value).replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
    }

    private String fit(final String value, final int length) {
        String text = text(value);
        if (text.length() <= length) {
            return text;
        }
        return text.substring(0, length - 3) + "...";
    }

    private String text(final String value) {
        return value == null ? "" : value.replaceAll("[^\\x20-\\x7E]", "?");
    }

    private record ReportDocument(List<String> lines, List<Chart> charts) {
    }

    private record Chart(String title, String subtitle, List<String> xLabels, List<ChartSeries> series) {
    }

    private record ChartSeries(String name, PdfColor color, List<Long> values) {
    }

    private record PdfColor(double red, double green, double blue) {
    }
}
