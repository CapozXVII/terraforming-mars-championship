package it.capozxvii.terraformingmars.model.enums.prelude;

import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import lombok.Getter;
@Getter
public enum PreludeEnum {
    ACQUIRED_SPACE_AGENCY("Acquired Space Agency"),
    ALLIED_BANKS("Allied Banks"),
    AQUIFER_TURBINES("Aquifer Turbines"),
    BIOFUELS("Biofuels"),
    BIOLAB("Biolab"),
    BIOSPHERE_SUPPORT("Biosphere Support"),
    BUSINESS_EMPIRE("Business Empire"),
    DOME_FARMING("Dome Farming"),
    DONATION("Donation"),
    EARLY_SETTLEMENT("Early Settlement"),
    ECOLOGY_EXPERTS("Ecology Experts"),
    EXCENTRIC_SPONSOR("Excentric Sponsor"),
    ECOLOGICAL_ZONE("Ecological Zone"),
    EXPERIMENTAL_FOREST("Experiment Forest"),
    GALILEAN_MINING("Galilean Mining"),
    GREAT_AQUIFER("Great Aquifer"),
    HUGE_ASTEROID("Huge Asteroid"),
    IO_RESERACH_OUTPOST("IO Reserva Outpost"),
    LOAN("Loan"),
    MARTIAN_INDUSTRIES("Martian Industries"),
    METAL_RICH_ASTEROID("Metal-Rich Asteroid"),
    METALS_COMPANY("Metals Company"),
    MINING_OPERATION("Mining Operation"),
    MOHOLE("Mohole"),
    MOHOLE_EXCAVATION("Mohole Excavation"),
    NITROGEN_SHIPMENT("Nitrogen Shipment"),
    ORBITAL_CONSTRUCTION_YARD("Orbital Construction Yard"),
    POLAR_INDUSTRIES("Polar Industries"),
    POWER_GENERATION("Power Generation"),
    RESEARCH_NETWORK("Research Network"),
    SELF_SUFFICIENT_SETTLEMENT("Self-Sufficient Settlement"),
    SMELTING_PLANT("Smelting Plant"),
    SOCIETY_SUPPORT("Society Support"),
    SUPPLIER("Supplier"),
    SUPPLY_DROP("Supply Drop"),
    UNMI_CONTRACTOR("UNMI Contractor");

    private final String name;

    PreludeEnum(final String name) {
        this.name = name;
    }

    public static PreludeEnum findPreludeByName(final String name) {
        for (final PreludeEnum preludeEnum : PreludeEnum.values()) {
            if (preludeEnum.getName().equals(name)) {
                return preludeEnum;
            }
        }
        throw new TerraformingMarsException(Message.NO_PRELUDE_CARD_FOUND, name);
    }
}
