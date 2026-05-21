package it.capozxvii.terraformingmars.abstracts;

import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Game;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.repository.ChampionshipRepository;
import it.capozxvii.terraformingmars.repository.DraftingRepository;
import it.capozxvii.terraformingmars.repository.GameRepository;
import it.capozxvii.terraformingmars.repository.PlayerRepository;
import it.capozxvii.terraformingmars.repository.PointsRepository;
import it.capozxvii.terraformingmars.service.IChampionshipService;
import it.capozxvii.terraformingmars.service.IDraftingService;
import it.capozxvii.terraformingmars.service.IGameService;
import it.capozxvii.terraformingmars.service.IPlayerService;
import it.capozxvii.terraformingmars.service.IReportService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractServiceTest extends AbstractTest {

    @Autowired
    protected IChampionshipService championshipService;
    @Autowired
    protected IGameService gameService;
    @Autowired
    protected IPlayerService playerService;
    @Autowired
    protected IDraftingService draftingService;
    @Autowired
    protected IReportService reportService;
    @Autowired
    protected ChampionshipRepository championshipRepository;
    @Autowired
    protected GameRepository gameRepository;
    @Autowired
    protected PointsRepository pointsRepository;
    @Autowired
    protected PlayerRepository playerRepository;
    @Autowired
    protected DraftingRepository draftingRepository;

    protected Championship genericChampionship;

    protected Game genericGame;
    protected Player genericPlayer;


    @BeforeAll
    void createGenericChampionship() {
        championshipRepository.findByName("La casa di Clank Generic Edition")
                .ifPresentOrElse(championship -> this.genericChampionship = championship, () -> {
                    this.genericChampionship = championshipRepository.save(
                            createChampionship("La casa di Clank Generic Edition", LocalDateTime.now(),
                                               LocalDateTime.now().plusYears(1)));
                });
    }

    @BeforeAll
    void createGenericGame() {
        LocalDateTime date = LocalDateTime.of(2024, 1, 1, 1, 1);
        gameRepository.findById(1L).ifPresentOrElse(game -> this.genericGame = game, () -> {
            this.genericGame = gameRepository.save(createGame("Clank House", genericChampionship, date));
        });
    }

    @BeforeAll
    void createGenericPlayer() {
        playerRepository.findById(1L).ifPresentOrElse(player -> {
            this.genericPlayer = player;
        }, () -> this.genericPlayer = playerRepository.saveAndFlush(createPlayer("genericPlayer", "genericFullname")));
    }
}
