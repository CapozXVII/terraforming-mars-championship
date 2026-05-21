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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
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
    private static final DateTimeFormatter GAME_DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm", Locale.ROOT);

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
        return createPdf(buildReportLines(championshipMapper.toDto(championship)));
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
        addLine(lines, String.format(Locale.ROOT, "%-30s %10s", "Player", "Points"));
        addLine(lines, String.format(Locale.ROOT, "%-30s %10s", "------------------------------", "----------"));

        Map<String, Long> standing = championship.getStanding() != null
                                     ? championship.getStanding().getPlayersPoints()
                                     : Map.of();
        if (standing == null || standing.isEmpty()) {
            addLine(lines, "No standing available");
            return;
        }
        standing.entrySet().stream().sorted(this::compareStandingRows).forEach(entry -> addLine(
                lines, String.format(Locale.ROOT, "%-30s %10d", fit(entry.getKey(), 30), entry.getValue())));
    }

    private int compareStandingRows(final Map.Entry<String, Long> first, final Map.Entry<String, Long> second) {
        int pointsComparison = Long.compare(second.getValue(), first.getValue());
        if (pointsComparison != 0) {
            return pointsComparison;
        }
        return first.getKey().compareTo(second.getKey());
    }

    private void addGames(final List<String> lines, final ChampionshipDto championship) {
        addLine(lines, "Games");
        List<GameDto> games = championship.getGames() == null
                              ? List.of()
                              : championship.getGames().stream().sorted(gameComparator()).toList();
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
                                         fit(pointsDto.getPlayer().getNickname(), 18),
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
        return Comparator.comparing(pointsDto -> pointsDto.getPlayer().getNickname());
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

    private byte[] createPdf(final List<String> lines) {
        List<List<String>> pages = splitPages(lines);
        StringBuilder pdf = new StringBuilder("%PDF-1.4\n");
        List<Integer> offsets = new ArrayList<>();
        int totalObjects = 3 + pages.size() * 2;

        appendObject(pdf, offsets, 1, "<< /Type /Catalog /Pages 2 0 R >>");
        appendObject(pdf, offsets, 2, pagesObject(pages.size()));
        appendObject(pdf, offsets, 3, "<< /Type /Font /Subtype /Type1 /BaseFont /Courier >>");

        for (int pageIndex = 0; pageIndex < pages.size(); pageIndex++) {
            int pageObjectId = 4 + pageIndex * 2;
            int contentObjectId = pageObjectId + 1;
            appendObject(pdf, offsets, pageObjectId, pageObject(contentObjectId));
            appendObject(pdf, offsets, contentObjectId, contentObject(pages.get(pageIndex)));
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

    private String contentObject(final List<String> lines) {
        String content = contentStream(lines);
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
}
