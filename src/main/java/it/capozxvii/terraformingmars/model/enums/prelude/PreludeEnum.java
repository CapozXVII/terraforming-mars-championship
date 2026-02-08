package it.capozxvii.terraformingmars.model.enums.prelude;

import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import lombok.Getter;
@Getter
public enum PreludeEnum {
    ACQUIRED_SPACE_AGENCY("Acquired Space Agency"),
    ALLIED_BANKS("Allied Banks"),
    APPLIED_SCIENCE("Applied Science"),
    AQUIFER_TURBINES("Aquifer Turbines"),
    ATMOSPHERIC_ENHANCERS("Atmospheric Enhancers"),
    BIOFUELS("Biofuels"),
    BIOLAB("Biolab"),
    BIOSPHERE_SUPPORT("Biosphere Support"),
    BOARD_OF_DIRECTORS("Board of Directors"),
    BUSINESS_EMPIRE("Business Empire"),
    COLONY_TRADE_HUB("Colony Trade Hub"),
    CORRIDORS_OF_POWER("Corridors of Power"),
    DOME_FARMING("Dome Farming"),
    DONATION("Donation"),
    EARLY_COLONIZATION("Early Colonization"),
    EARLY_SETTLEMENT("Early Settlement"),
    ECOLOGICAL_ZONE("Ecological Zone"),
    ECOLOGY_EXPERTS("Ecology Experts"),
    EXCENTRIC_SPONSOR("Excentric Sponsor"),
    EXPERIMENT_FOREST("Experiment Forest"),
    FLOATING_TRADE_HUB("Floating Trade Hub"),
    FOCUSED_ORGANIZATION("Focused Organization"),
    GALILEAN_MINING("Galilean Mining"),
    GREAT_AQUIFER("Great Aquifer"),
    HIGH_CIRCLES("High Circles"),
    HUGE_ASTEROID("Huge Asteroid"),
    INDUSTRIAL_COMPLEX("Industrial Complex"),
    IO_RESERACH_OUTPOST("IO Research Outpost"),
    LOAN("Loan"),
    MAIN_BELT_ASTEROIDS("Main Belt Asteroids"),
    MARTIAN_INDUSTRIES("Martian Industries"),
    METAL_RICH_ASTEROID("Metal-Rich Asteroid"),
    METALS_COMPANY("Metals Company"),
    MINING_OPERATION("Mining Operation"),
    MOHOLE("Mohole"),
    MOHOLE_EXCAVATION("Mohole Excavation"),
    NITROGEN_SHIPMENT("Nitrogen Shipment"),
    NOBEL_PRIZE("Nobel Prize"),
    OLD_MINING_COLONY("Old Mining Colony"),
    ORBITAL_CONSTRUCTION_YARD("Orbital Construction Yard"),
    PLANETARY_ALLIANCE("Planetary Alliance"),
    POLAR_INDUSTRIES("Polar Industries"),
    POWER_GENERATION("Power Generation"),
    PRESERVATION_PROGRAM("Preservation Program"),
    PROJECT_EDEN("Project Eden"),
    RECESSION("Recession"),
    RESEARCH_NETWORK("Research Network"),
    RISE_TO_POWER("Rise To Power"),
    SELF_SUFFICIENT_SETTLEMENT("Self-Sufficient Settlement"),
    SMELTING_PLANT("Smelting Plant"),
    SOCIETY_SUPPORT("Society Support"),
    SOIL_BACTERIA("Soil Bacteria"),
    SPACE_LANES("Space Lanes"),
    SUPPLIER("Supplier"),
    SUPPLY_DROP("Supply Drop"),
    TERRAFORMING_DEAL("Terraforming Deal"),
    UNMI_CONTRACTOR("UNMI Contractor"),
    VENUS_CONTRACT("Venus Contract"),
    VENUS_L1_SHADE("Venus L1 Shade"),
    WORLD_GOVERNMENT_ADVISOR("World Government Advisor");

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
