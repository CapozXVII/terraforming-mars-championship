package it.capozxvii.terraformingmars.abstracts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.capozxvii.terraformingmars.model.dto.CorporationsExpansionPairDto;
import it.capozxvii.terraformingmars.model.dto.DraftingDto;
import it.capozxvii.terraformingmars.model.dto.GameDto;
import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.model.dto.PointsDto;
import it.capozxvii.terraformingmars.model.enums.corporation.Corporation;
import it.capozxvii.terraformingmars.model.enums.prelude.PreludeEnum;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Drafting;
import it.capozxvii.terraformingmars.model.jpa.Game;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.Points;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractTest {
    protected static void filterAndCheckGameDtos(final List<GameDto> gameDtos, final Game game) {
        Optional<GameDto> gameDtoOptional =
                gameDtos.stream().filter(gameDto -> Objects.equals(gameDto.getId(), game.getGameId())).findFirst();

        assertTrue(gameDtoOptional.isPresent());
        GameDto gameDto = gameDtoOptional.get();
        assertEquals(game.getGameId(), gameDto.getId());
        assertEquals(game.getLocation(), gameDto.getLocation());
        assertEquals(game.getGameDate(), gameDto.getGameDate());
    }

    protected Championship createChampionship(final String name, final LocalDateTime start, final LocalDateTime end) {
        return Championship.builder().name(name).startingDate(start).endingDate(end).build();
    }

    protected Game createGame(final String location, final Championship championship, final LocalDateTime gameDate) {
        return Game.builder().location(location).championship(championship).gameDate(gameDate).build();
    }

    protected Player createPlayer(final String nickname, final String fullname) {
        return Player.builder().nickname(nickname).fullname(fullname).build();
    }
    
    protected Drafting createDrafting(final Player player, final Map<Integer, CorporationsExpansionPairDto> draftings,
            final Championship championship) {
        return Drafting.builder().draftings(draftings)
                .player(player)
                .championship(championship).build();
    }

    protected Points createPoints(
            final int terraformingRating,
            final int greenery,
            final int city,
            final int milestones,
            final int awards,
            final int cards,
            final Map<String, Integer> otherCategories,
            final Corporation corporation,
            final PreludeEnum firstPrelude,
            final PreludeEnum secondPrelude,
            final Player player) {
        return Points.builder().terraformingRating(terraformingRating)
                .greenery(greenery)
                .city(city)
                .milestones(milestones)
                .awards(awards)
                .cards(cards)
                .firstPrelude(firstPrelude)
                .secondPrelude(secondPrelude)
                .otherCategories(otherCategories)
                .corporation(corporation).player(player).build();
    }

    protected GameDto createGameDto(final LocalDateTime gameDate, final String location, final Long championshipId) {
        return GameDto.builder().gameDate(gameDate).location(location).points(new ArrayList<>())
                .championshipId(championshipId).build();
    }

    protected PlayerDto createPlayerDto(final String nickname, final String fullname, final Long id) {
        return PlayerDto.builder().nickname(nickname).fullname(fullname).id(id).build();
    }

    protected PointsDto createPointsDto(
            final int terraformingRating,
            final int greenery,
            final int city,
            final int milestones,
            final int awards,
            final int cards,
            final Corporation corporation,
            final PreludeEnum firstPrelude,
            final PreludeEnum secondPrelude,
            final Map<String, Integer> otherCategories,
            final PlayerDto playerDto) {
        return PointsDto.builder().terraformingRating(terraformingRating)
                .greenery(greenery)
                .city(city)
                .milestones(milestones)
                .awards(awards)
                .cards(cards)
                .firstPrelude(firstPrelude.getName())
                .secondPrelude(secondPrelude.getName())
                .otherCategories(otherCategories)
                .corporation(corporation.getName()).player(playerDto).build();
    }

    protected DraftingDto createDraftingDto(final Long championshipId,
            final PlayerDto playerDto,
            final Map<Integer, CorporationsExpansionPairDto> chosenCorps) {
        return DraftingDto.builder().championshipId(championshipId).draftings(chosenCorps)
                .player(playerDto).build();
    }

    protected void checkPointsDto(
            final Corporation corporation,
            final int terraformingRating,
            final int greenery,
            final int city,
            final int milestones,
            final int awards,
            final int cards,
            final PreludeEnum firstPrelude,
            final PreludeEnum secondPrelude,
            final Map<String, Integer> otherCategories,
            final Long playerId,
            final PointsDto pointsDto) {
        assertEquals(corporation.getName(), pointsDto.getCorporation());
        assertEquals(playerId, pointsDto.getPlayer().getId());
        assertEquals(terraformingRating, pointsDto.getTerraformingRating());
        assertEquals(greenery, pointsDto.getGreenery());
        assertEquals(city, pointsDto.getCity());
        assertEquals(milestones, pointsDto.getMilestones());
        assertEquals(awards, pointsDto.getAwards());
        assertEquals(cards, pointsDto.getCards());
        assertEquals(otherCategories, pointsDto.getOtherCategories());
        assertEquals(firstPrelude.getName(), pointsDto.getFirstPrelude());
        assertEquals(secondPrelude.getName(), pointsDto.getSecondPrelude());
    }

    protected void filterAndCheckPoints(
            final Set<Points> points,
            final Corporation corporation,
            final int terraformingRating,
            final int greenery,
            final int city,
            final int milestones,
            final int awards,
            final int cards,
            final PreludeEnum firstPrelude,
            final PreludeEnum secondPrelude,
            final Map<String, Integer> otherCategories,
            final Player player) {

        Optional<Points> pointsOpt =
                points.stream().filter(point -> point.getPlayer().getNickname().equals(player.getNickname()))
                .findFirst();
        assertTrue(pointsOpt.isPresent());
        Points point = pointsOpt.get();
        assertEquals(corporation, point.getCorporation());
        assertEquals(terraformingRating, point.getTerraformingRating());
        assertEquals(greenery, point.getGreenery());
        assertEquals(city, point.getCity());
        assertEquals(milestones, point.getMilestones());
        assertEquals(awards, point.getAwards());
        assertEquals(cards, point.getCards());
        assertEquals(otherCategories, point.getOtherCategories());
        assertEquals(firstPrelude, point.getFirstPrelude());
        assertEquals(secondPrelude, point.getSecondPrelude());
        assertEquals(otherCategories, point.getOtherCategories());
    }

}
