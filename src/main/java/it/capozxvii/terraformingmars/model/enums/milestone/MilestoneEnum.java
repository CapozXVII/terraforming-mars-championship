package it.capozxvii.terraformingmars.model.enums.milestone;

import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import lombok.Getter;
@Getter
public enum MilestoneEnum {
    BRIBER("Briber"),
    BUILDER("Builder"),
    COASTGUARD("Coastguard"),
    DIVERSIFIER("Diversifier"),
    ECOLOGIST("Ecologist"),
    ENERGIZER("Energizer"),
    ENGINEER("Engineer"),
    FARMER("Farmer"),
    FORESTER("Forester"),
    FUNDRAISER("Fundraiser"),
    GARDENER("Gardener"),
    GENERALIST("Generalist"),
    GEOLOGIST("Geologist"),
    HYDROLOGIST("Hydrologist"),
    LANDSHAPER("Landshaper"),
    LEGEND("Legend"),
    LOBBYIST("Lobbyist"),
    MAYOR("Mayor"),
    MERCHANT("Merchant"),
    METALLURGIST("Metallurgist"),
    PHILANTHROPIST("Philanthropist"),
    PIONEER("Pioneer"),
    PLANETOLOGIST("Planetologist"),
    PLANNER("Planner"),
    PRODUCER("Producer"),
    RESEARCHER("Researcher"),
    RIM_SETTLER("Rim Settler"),
    SPACEFARER("Spacefarer"),
    SPONSOR("Sponsor"),
    TACTICIAN("Tactician"),
    TERRAFORMER("Terraformer"),
    TERRAN("Terran"),
    THAWER("Thawer"),
    TRADER("Trader");

    private final String name;

    MilestoneEnum(final String name) {
        this.name = name;
    }

    public static MilestoneEnum findMilestoneByName(final String name) {
        for (final MilestoneEnum milestone : MilestoneEnum.values()) {
            if (milestone.getName().equals(name)) {
                return milestone;
            }
        }
        throw new TerraformingMarsException(Message.NO_MILESTONE_WAS_FOUND, name);
    }
}
