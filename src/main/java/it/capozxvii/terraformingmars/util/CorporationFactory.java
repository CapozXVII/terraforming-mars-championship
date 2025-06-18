package it.capozxvii.terraformingmars.util;

import it.capozxvii.terraformingmars.model.enums.corporation.ColoniesCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.CorporateEraCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.Corporation;
import it.capozxvii.terraformingmars.model.enums.corporation.PreludeCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.TurmoilCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.VenusNextCorporations;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.util.List;
public final class CorporationFactory {
    
    private CorporationFactory() {
    }

    private static final List<Class<? extends Corporation>> CORPORATION_ENUMS = List.of(
            CorporateEraCorporations.class,
            PreludeCorporations.class,
            VenusNextCorporations.class,
            TurmoilCorporations.class,
            ColoniesCorporations.class
    );

    public static Corporation fromName(final String name) {
        for (Class<? extends Corporation> enumClass : CORPORATION_ENUMS) {
            for (Corporation corp : enumClass.getEnumConstants()) {
                if (corp.getName().equalsIgnoreCase(name)) {
                    return corp;
                }
            }
        }
        throw new TerraformingMarsException(Message.NO_CORPORATION_WAS_FOUND, name);
    }
}
