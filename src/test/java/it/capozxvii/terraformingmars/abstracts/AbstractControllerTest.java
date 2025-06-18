package it.capozxvii.terraformingmars.abstracts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.capozxvii.terraformingmars.service.IChampionshipService;
import it.capozxvii.terraformingmars.service.IDraftingService;
import it.capozxvii.terraformingmars.service.IGameService;
import it.capozxvii.terraformingmars.service.IPlayerService;
import it.capozxvii.terraformingmars.service.IPointsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractTest {

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected IChampionshipService championshipService;

    @MockBean
    protected IGameService gameService;

    @MockBean
    protected IPlayerService playerService;

    @MockBean
    protected IPointsService pointsService;

    @MockBean
    protected IDraftingService previsionService;

    @BeforeAll
    public static void setup() throws Exception {
        MAPPER.registerModule(new JavaTimeModule());
    }
}
