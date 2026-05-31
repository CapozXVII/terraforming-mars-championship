package it.capozxvii.terraformingmars.controller;

import static it.capozxvii.terraformingmars.controller.ControllerExceptionInterceptor.UNKNOWN_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.capozxvii.terraformingmars.abstracts.AbstractControllerTest;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.SimpleWrapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@WebMvcTest(ReportController.class)
class ReportControllerTest extends AbstractControllerTest {

    @Test
    void createChampionshipReportTest() throws Exception {
        byte[] report = "%PDF-1.4".getBytes(StandardCharsets.ISO_8859_1);
        when(reportService.createChampionshipReport(1L)).thenReturn(report);

        byte[] res = mvc.perform(get("/report/championship-report").param("championshipId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                                           "attachment; filename=championship-1-report.pdf"))
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        assertArrayEquals(report, res);
    }

    @Test
    void createChampionshipReportExceptionTest() throws Exception {
        TerraformingMarsException exception = new TerraformingMarsException("Unknown error");
        when(reportService.createChampionshipReport(1L)).thenThrow(exception);

        String content = mvc.perform(get("/report/championship-report").param("championshipId", "1"))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String res = MAPPER.readValue(content, new TypeReference<SimpleWrapper<?>>() {
        }).getMessage();

        assertEquals("Unknown error", res);
    }

    @Test
    void createChampionshipReportUnknownExceptionTest() throws Exception {
        when(reportService.createChampionshipReport(1L)).thenThrow(new RuntimeException("Unexpected error"));

        String content = mvc.perform(get("/report/championship-report").param("championshipId", "1"))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String res = MAPPER.readValue(content, new TypeReference<SimpleWrapper<?>>() {
        }).getMessage();

        assertEquals(UNKNOWN_ERROR_MESSAGE, res);
    }
}
