package it.capozxvii.terraformingmars.service.impl;

import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;
import it.capozxvii.terraformingmars.model.dto.CorporationsExpansionPairDto;
import it.capozxvii.terraformingmars.model.dto.DraftingDto;
import it.capozxvii.terraformingmars.model.dto.GameDto;
import it.capozxvii.terraformingmars.model.dto.PointsDto;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.mapper.ChampionshipMapper;
import it.capozxvii.terraformingmars.model.mapper.DraftingMapper;
import it.capozxvii.terraformingmars.repository.ChampionshipRepository;
import it.capozxvii.terraformingmars.repository.DraftingRepository;
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
import java.util.Objects;
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
    private static final int REPORT_FONT_SIZE = 9;
    private static final double REPORT_CHARACTER_WIDTH = REPORT_FONT_SIZE * 0.6;
    private static final String REGULAR_FONT = "F1";
    private static final String BOLD_FONT = "F2";
    private static final int CHART_LEFT = 62;
    private static final int CHART_BOTTOM = 300;
    private static final int CHART_WIDTH = 460;
    private static final int CHART_HEIGHT = 385;
    private static final String DRAFTING_INDENT = "  ";
    private static final int DRAFTING_INDENT_LENGTH = 2;
    private static final int DRAFTING_GAME_COLUMN_LENGTH = 6;
    private static final int DRAFTING_EXPANSION_COLUMN_LENGTH = 24;
    private static final int DRAFTING_FIRST_EXPANSION_COLUMN = DRAFTING_INDENT_LENGTH
                                                               + DRAFTING_GAME_COLUMN_LENGTH + 1;
    private static final int DRAFTING_SECOND_EXPANSION_COLUMN = DRAFTING_FIRST_EXPANSION_COLUMN
                                                                + DRAFTING_EXPANSION_COLUMN_LENGTH + 1;
    private static final String DRAFTING_ROW_FORMAT = DRAFTING_INDENT + "%-" + DRAFTING_GAME_COLUMN_LENGTH + "s %-"
                                                      + DRAFTING_EXPANSION_COLUMN_LENGTH + "s %-"
                                                      + DRAFTING_EXPANSION_COLUMN_LENGTH + "s";
    private static final DateTimeFormatter GAME_DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm", Locale.ROOT);
    private static final PdfColor BLACK = new PdfColor(0.00, 0.00, 0.00);
    private static final PdfColor DRAFTING_HIGHLIGHT_COLOR = new PdfColor(1.00, 0.92, 0.20);
    private static final PdfColor DRAFTING_STRIKETHROUGH_COLOR = new PdfColor(0.80, 0.05, 0.05);
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
    private final DraftingRepository draftingRepository;
    private final ChampionshipMapper championshipMapper;
    private final DraftingMapper draftingMapper;

    public ReportService(final ChampionshipRepository championshipRepository,
            final DraftingRepository draftingRepository,
            final ChampionshipMapper championshipMapper,
            final DraftingMapper draftingMapper) {
        this.championshipRepository = championshipRepository;
        this.draftingRepository = draftingRepository;
        this.championshipMapper = championshipMapper;
        this.draftingMapper = draftingMapper;
    }

    @Override
    @Transactional
    public byte[] createChampionshipReport(final Long championshipId) {
        Championship championship = championshipRepository.findById(championshipId).orElseThrow(
                () -> new TerraformingMarsException(Message.NOT_FOUND, Championship.class.getSimpleName(),
                                                    championshipId));
        List<DraftingDto> draftings = draftingRepository.getDraftingsByChampionship(championship).stream()
                .map(draftingMapper::toDto)
                .toList();
        return createPdf(buildReport(championshipMapper.toDto(championship), draftings));
    }

    private ReportDocument buildReport(final ChampionshipDto championship, final List<DraftingDto> draftings) {
        return new ReportDocument(buildReportLines(championship), buildCharts(championship),
                                  buildDraftingLines(draftings));
    }

    private List<ReportLine> buildReportLines(final ChampionshipDto championship) {
        List<ReportLine> lines = new ArrayList<>();
        addBoldLine(lines, "Championship report");
        addBoldLine(lines, "Championship: " + text(championship.getName()));
        addLine(lines, "");
        addStanding(lines, championship);
        addLine(lines, "");
        addGames(lines, championship);
        return lines;
    }

    private List<ReportLine> buildDraftingLines(final List<DraftingDto> draftings) {
        List<ReportLine> lines = new ArrayList<>();
        addDraftings(lines, draftings);
        return lines;
    }

    private void addStanding(final List<ReportLine> lines, final ChampionshipDto championship) {
        addBoldLine(lines, "Current standing");
        addBoldLine(lines, String.format(Locale.ROOT, "%-30s %10s %14s %16s", "Player", "Points",
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

    private void addDraftings(final List<ReportLine> lines, final List<DraftingDto> draftings) {
        addBoldLine(lines, "Drafting choices");
        addLine(lines, "Legend: yellow highlight = chosen expansion; red strikethrough = discarded expansion.");
        addLine(lines, "");
        if (draftings == null || draftings.isEmpty()) {
            addLine(lines, "No drafting available");
            return;
        }
        draftings.stream().sorted(draftingComparator()).forEach(drafting -> addDraftingRows(lines, drafting));
    }

    private Comparator<DraftingDto> draftingComparator() {
        return Comparator.comparing(this::draftingPlayerName)
                .thenComparing(DraftingDto::getId, Comparator.nullsLast(Comparator.naturalOrder()));
    }

    private void addDraftingRows(final List<ReportLine> lines, final DraftingDto drafting) {
        addBoldLine(lines, "Player: " + draftingPlayerName(drafting));
        if (drafting.getDraftings() == null || drafting.getDraftings().isEmpty()) {
            addLine(lines, DRAFTING_INDENT + "No drafting choices");
            addLine(lines, "");
            return;
        }
        addBoldLine(lines, String.format(Locale.ROOT, DRAFTING_ROW_FORMAT, "Game", "First expansion",
                                         "Second expansion"));
        addLine(lines, String.format(Locale.ROOT, DRAFTING_ROW_FORMAT, "------",
                                     "------------------------", "------------------------"));
        drafting.getDraftings().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> addDraftingRow(lines, entry.getKey(), entry.getValue()));
        addLine(lines, "");
    }

    private void addDraftingRow(final List<ReportLine> lines, final Long gameNumber,
            final CorporationsExpansionPairDto pair) {
        CorporationsExpansionPairDto safePair = pair == null ? CorporationsExpansionPairDto.builder().build() : pair;
        String gameLabel = "G" + gameNumber;
        String firstExpansion = fit(safePair.getFirstExpansion(), DRAFTING_EXPANSION_COLUMN_LENGTH);
        String secondExpansion = fit(safePair.getSecondExpansion(), DRAFTING_EXPANSION_COLUMN_LENGTH);
        String row = String.format(Locale.ROOT, DRAFTING_ROW_FORMAT,
                                   fit(gameLabel, DRAFTING_GAME_COLUMN_LENGTH),
                                   firstExpansion,
                                   secondExpansion);
        List<LineDecoration> decorations = draftingDecorations(safePair, firstExpansion, secondExpansion);
        lines.add(new ReportLine(row, decorations, false, List.of()));
    }

    private List<LineDecoration> draftingDecorations(final CorporationsExpansionPairDto pair,
            final String firstExpansion, final String secondExpansion) {
        if (pair == null || pair.getChosenExpansion() == null) {
            return List.of();
        }
        List<LineDecoration> decorations = new ArrayList<>();
        if (sameExpansion(pair.getChosenExpansion(), pair.getFirstExpansion())) {
            decorations.add(new LineDecoration(DecorationType.HIGHLIGHT, DRAFTING_FIRST_EXPANSION_COLUMN,
                                               firstExpansion.length()));
            decorations.add(new LineDecoration(DecorationType.STRIKETHROUGH, DRAFTING_SECOND_EXPANSION_COLUMN,
                                               secondExpansion.length()));
        } else if (sameExpansion(pair.getChosenExpansion(), pair.getSecondExpansion())) {
            decorations.add(new LineDecoration(DecorationType.STRIKETHROUGH, DRAFTING_FIRST_EXPANSION_COLUMN,
                                               firstExpansion.length()));
            decorations.add(new LineDecoration(DecorationType.HIGHLIGHT, DRAFTING_SECOND_EXPANSION_COLUMN,
                                               secondExpansion.length()));
        }
        return decorations;
    }

    private boolean sameExpansion(final String firstExpansion, final String secondExpansion) {
        return Objects.equals(text(firstExpansion).toLowerCase(Locale.ROOT),
                              text(secondExpansion).toLowerCase(Locale.ROOT));
    }

    private String draftingPlayerName(final DraftingDto drafting) {
        return drafting.getPlayer() == null ? "" : text(drafting.getPlayer().getNickname());
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

    private void addGames(final List<ReportLine> lines, final ChampionshipDto championship) {
        addBoldLine(lines, "Games");
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

    private void addGame(final List<ReportLine> lines, final GameDto game, final int gameNumber) {
        addBoldLine(lines, "Game " + gameNumber + ": " + text(game.getLocation()) + " - " + formattedDate(game));
        addBoldLine(lines, String.format(Locale.ROOT, "%-18s %4s %7s %4s %10s %6s %5s %5s %5s",
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
            addOtherCategoriesLine(lines, pointsDto);
            addMixedLine(lines, List.of(boldText("  Chosen corporation"),
                                        regularText(": " + text(pointsDto.getCorporation()))));
            addMixedLine(lines, List.of(boldText("  Preludes"),
                                        regularText(": " + text(pointsDto.getFirstPrelude()) + " / "
                                                    + text(pointsDto.getSecondPrelude()))));
        });
    }

    private void addOtherCategoriesLine(final List<ReportLine> lines, final PointsDto pointsDto) {
        List<TextRun> textRuns = new ArrayList<>();
        textRuns.add(boldText("  Other categories"));
        textRuns.add(regularText(": "));
        if (pointsDto.getOtherCategories() == null || pointsDto.getOtherCategories().isEmpty()) {
            textRuns.add(regularText("None"));
            addMixedLine(lines, textRuns);
            return;
        }
        new TreeMap<>(pointsDto.getOtherCategories()).forEach((category, points) -> {
            if (textRuns.size() > 2) {
                textRuns.add(regularText(", "));
            }
            textRuns.add(boldText(text(category)));
            textRuns.add(regularText("=" + points));
        });
        addMixedLine(lines, textRuns);
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

    private void addLine(final List<ReportLine> lines, final String line) {
        addStyledLine(lines, line, false);
    }

    private void addBoldLine(final List<ReportLine> lines, final String line) {
        addStyledLine(lines, line, true);
    }

    private void addStyledLine(final List<ReportLine> lines, final String line, final boolean bold) {
        if (line.length() <= MAX_LINE_LENGTH) {
            lines.add(new ReportLine(line, List.of(), bold, List.of()));
            return;
        }
        int start = 0;
        while (start < line.length()) {
            int end = Math.min(start + MAX_LINE_LENGTH, line.length());
            lines.add(new ReportLine(line.substring(start, end), List.of(), bold, List.of()));
            start = end;
        }
    }

    private void addMixedLine(final List<ReportLine> lines, final List<TextRun> textRuns) {
        List<TextRun> currentLine = new ArrayList<>();
        int currentLength = 0;
        for (TextRun textRun : textRuns) {
            String remaining = textRun.text();
            while (!remaining.isEmpty()) {
                int availableLength = MAX_LINE_LENGTH - currentLength;
                if (availableLength == 0) {
                    addTextRunsLine(lines, currentLine);
                    currentLine = new ArrayList<>();
                    currentLength = 0;
                    availableLength = MAX_LINE_LENGTH;
                }
                int end = Math.min(availableLength, remaining.length());
                currentLine.add(new TextRun(remaining.substring(0, end), textRun.bold()));
                currentLength += end;
                remaining = remaining.substring(end);
            }
        }
        if (!currentLine.isEmpty()) {
            addTextRunsLine(lines, currentLine);
        }
    }

    private void addTextRunsLine(final List<ReportLine> lines, final List<TextRun> textRuns) {
        String line = textRuns.stream().map(TextRun::text).collect(Collectors.joining());
        lines.add(new ReportLine(line, List.of(), false, textRuns));
    }

    private TextRun boldText(final String text) {
        return new TextRun(text, true);
    }

    private TextRun regularText(final String text) {
        return new TextRun(text, false);
    }

    private byte[] createPdf(final ReportDocument reportDocument) {
        List<String> pageContentStreams = new ArrayList<>();
        splitPages(reportDocument.lines()).stream().map(this::contentStream).forEach(pageContentStreams::add);
        reportDocument.charts().stream().map(this::chartContentStream).forEach(pageContentStreams::add);
        splitPages(reportDocument.draftingLines()).stream()
                .map(this::contentStream)
                .forEach(pageContentStreams::add);

        StringBuilder pdf = new StringBuilder("%PDF-1.4\n");
        List<Integer> offsets = new ArrayList<>();
        int totalObjects = 4 + pageContentStreams.size() * 2;

        appendObject(pdf, offsets, 1, "<< /Type /Catalog /Pages 2 0 R >>");
        appendObject(pdf, offsets, 2, pagesObject(pageContentStreams.size()));
        appendObject(pdf, offsets, 3, "<< /Type /Font /Subtype /Type1 /BaseFont /Courier >>");
        appendObject(pdf, offsets, 4, "<< /Type /Font /Subtype /Type1 /BaseFont /Courier-Bold >>");

        for (int pageIndex = 0; pageIndex < pageContentStreams.size(); pageIndex++) {
            int pageObjectId = 5 + pageIndex * 2;
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

    private List<List<ReportLine>> splitPages(final List<ReportLine> lines) {
        List<List<ReportLine>> pages = new ArrayList<>();
        for (int start = 0; start < lines.size(); start += MAX_LINES_PER_PAGE) {
            pages.add(lines.subList(start, Math.min(start + MAX_LINES_PER_PAGE, lines.size())));
        }
        if (pages.isEmpty()) {
            pages.add(List.of(new ReportLine("", List.of(), false, List.of())));
        }
        return pages;
    }

    private String pagesObject(final int pageCount) {
        String kids = "";
        for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
            kids = kids + (5 + pageIndex * 2) + " 0 R ";
        }
        return "<< /Type /Pages /Kids [" + kids + "] /Count " + pageCount + " >>";
    }

    private String pageObject(final int contentObjectId) {
        return "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 " + PAGE_HEIGHT
               + "] /Resources << /Font << /F1 3 0 R /F2 4 0 R >> >> /Contents "
               + contentObjectId + " 0 R >>";
    }

    private String contentObject(final String content) {
        int length = content.getBytes(StandardCharsets.ISO_8859_1).length;
        return "<< /Length " + length + " >>\nstream\n" + content + "\nendstream";
    }

    private String contentStream(final List<ReportLine> lines) {
        StringBuilder content = new StringBuilder();
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            int y = FIRST_LINE_Y - lineIndex * LINE_HEIGHT;
            appendHighlights(content, lines.get(lineIndex), y);
            appendFillColor(content, BLACK);
            appendReportLineText(content, lines.get(lineIndex), y);
            appendStrikethroughs(content, lines.get(lineIndex), y);
        }
        return content.toString();
    }

    private void appendReportLineText(final StringBuilder content, final ReportLine line, final int y) {
        if (line.textRuns().isEmpty()) {
            String font = line.bold() ? BOLD_FONT : REGULAR_FONT;
            appendText(content, line.text(), REPORT_FONT_SIZE, LEFT_MARGIN, y, font);
            return;
        }

        double x = LEFT_MARGIN;
        for (TextRun textRun : line.textRuns()) {
            String font = textRun.bold() ? BOLD_FONT : REGULAR_FONT;
            appendText(content, textRun.text(), REPORT_FONT_SIZE, x, y, font);
            x += textRun.text().length() * REPORT_CHARACTER_WIDTH;
        }
    }

    private void appendHighlights(final StringBuilder content, final ReportLine line, final int y) {
        line.decorations().stream()
                .filter(decoration -> decoration.type() == DecorationType.HIGHLIGHT)
                .forEach(decoration -> {
                    appendFillColor(content, DRAFTING_HIGHLIGHT_COLOR);
                    appendRectangle(content, decorationX(decoration), y - 2, decorationWidth(decoration), 10);
                });
    }

    private void appendStrikethroughs(final StringBuilder content, final ReportLine line, final int y) {
        line.decorations().stream()
                .filter(decoration -> decoration.type() == DecorationType.STRIKETHROUGH)
                .forEach(decoration -> {
                    appendStrokeColor(content, DRAFTING_STRIKETHROUGH_COLOR);
                    content.append("0.8 w\n");
                    appendLine(content, decorationX(decoration), y + 3,
                               decorationX(decoration) + decorationWidth(decoration), y + 3);
                });
    }

    private double decorationX(final LineDecoration decoration) {
        return LEFT_MARGIN + decoration.startColumn() * REPORT_CHARACTER_WIDTH;
    }

    private double decorationWidth(final LineDecoration decoration) {
        return Math.max(0, decoration.length()) * REPORT_CHARACTER_WIDTH;
    }

    private String chartContentStream(final Chart chart) {
        StringBuilder content = new StringBuilder();
        appendText(content, chart.title(), 14, LEFT_MARGIN, FIRST_LINE_Y, BOLD_FONT);
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
        appendText(content, value, fontSize, x, y, REGULAR_FONT);
    }

    private void appendText(final StringBuilder content, final String value, final int fontSize, final double x,
            final double y, final String font) {
        content.append(String.format(Locale.ROOT, "BT%n/%s %d Tf%n1 0 0 1 %.2f %.2f Tm%n(%s) Tj%nET%n",
                                     font, fontSize, x, y, escapePdf(value)));
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

    private record ReportDocument(List<ReportLine> lines, List<Chart> charts, List<ReportLine> draftingLines) {
    }

    private record ReportLine(String text, List<LineDecoration> decorations, boolean bold, List<TextRun> textRuns) {
    }

    private record TextRun(String text, boolean bold) {
    }

    private record LineDecoration(DecorationType type, int startColumn, int length) {
    }

    private enum DecorationType {
        HIGHLIGHT,
        STRIKETHROUGH
    }

    private record Chart(String title, String subtitle, List<String> xLabels, List<ChartSeries> series) {
    }

    private record ChartSeries(String name, PdfColor color, List<Long> values) {
    }

    private record PdfColor(double red, double green, double blue) {
    }
}
