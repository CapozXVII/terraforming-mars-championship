package it.capozxvii.terraformingmars.service;

import it.capozxvii.terraformingmars.model.dto.DraftingDto;
import java.util.List;

public interface IDraftingService {
    List<DraftingDto> insertDrafting(List<DraftingDto> draftingDtos);

    List<DraftingDto> editDrafting(DraftingDto draftingDto);

}
