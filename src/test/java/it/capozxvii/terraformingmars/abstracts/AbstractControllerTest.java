package it.capozxvii.terraformingmars.abstracts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.capozxvii.terraformingmars.service.IChampionshipService;
import it.capozxvii.terraformingmars.service.IDraftingService;
import it.capozxvii.terraformingmars.service.IGameService;
import it.capozxvii.terraformingmars.service.IPlayerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractTest {

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    protected MockMvc mvc;

    @MockitoBean
    protected IChampionshipService championshipService;

    @MockitoBean
    protected IGameService gameService;

    @MockitoBean
    protected IPlayerService playerService;

    @MockitoBean
    protected IDraftingService previsionService;

    @BeforeAll
    public static void setup() throws Exception {
        MAPPER.registerModule(new JavaTimeModule());
    }
}
