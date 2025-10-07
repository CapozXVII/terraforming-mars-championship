package it.capozxvii.terraformingmars.controller;

import it.capozxvii.terraformingmars.model.dto.DraftingDto;
import it.capozxvii.terraformingmars.service.IDraftingService;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.CollectionWrapper;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "Drafting Controller")
@RequestMapping(value = "/drafting")
public class DraftingController {

    private final IDraftingService draftingService;

    public DraftingController(final IDraftingService draftingService) {
        this.draftingService = draftingService;
    }

    @PostMapping(value = "/insert-draftings")
    public ResponseEntity<CollectionWrapper<DraftingDto>> insertDrafting(
            @RequestBody final List<DraftingDto> draftingDtos) {
        try {
            return ResponseEntity.ok(
                    CollectionWrapper.<DraftingDto>builder()
                            .responseObject(draftingService.insertDrafting(draftingDtos)).build());
        } catch (TerraformingMarsException terraformingMarsException) {
            return ResponseEntity.internalServerError()
                    .body(CollectionWrapper.<DraftingDto>builder().message(terraformingMarsException.getMessage())
                                  .build());
        }
    }

    @GetMapping(value = "/view-draftings")
    public ResponseEntity<CollectionWrapper<DraftingDto>> viewDraftings(
            @RequestParam("championshipId") final Long championshipId) {
        try {
            return ResponseEntity.ok(
                    CollectionWrapper.<DraftingDto>builder()
                            .responseObject(draftingService.viewDraftings(championshipId))
                            .build());
        } catch (TerraformingMarsException terraformingMarsException) {
            return ResponseEntity.internalServerError()
                    .body(CollectionWrapper.<DraftingDto>builder().message(terraformingMarsException.getMessage())
                                  .build());
        }
    }
}
