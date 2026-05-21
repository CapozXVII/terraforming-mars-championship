package it.capozxvii.terraformingmars.controller;

import it.capozxvii.terraformingmars.service.IReportService;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "Report Controller")
@RequestMapping(value = "/report")
public class ReportController {

    private final IReportService reportService;

    public ReportController(final IReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/championship-report")
    public ResponseEntity<byte[]> createChampionshipReport(
            @RequestParam("championshipId") final Long championshipId) {
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=championship-" + championshipId + "-report.pdf")
                    .body(reportService.createChampionshipReport(championshipId));
        } catch (TerraformingMarsException e) {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(e.getMessage().getBytes(StandardCharsets.UTF_8));
        }
    }
}
