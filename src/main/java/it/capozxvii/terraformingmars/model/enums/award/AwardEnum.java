package it.capozxvii.terraformingmars.model.enums.award;

import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import lombok.Getter;

@Getter
public enum AwardEnum {
    ADMINISTRATOR("Administrator"),
    BANKER("Banker"),
    BENEFACTOR("Benefactor"),
    BIOLOGIST("Biologist"),
    BOTANIST("Botanist"),
    CELEBRITY("Celebrity"),
    COLLECTOR("Collector"),
    CONSTRUCTOR("Constructor"),
    CONTRACTOR("Contractor"),
    CULTIVATOR("Cultivator"),
    ELECTRICIAN("Electrician"),
    ESTATE_DEALER("Estate Dealer"),
    EXCENTRIC("Excentric"),
    FORECASTER("Forecaster"),
    FOUNDER("Founder"),
    HIGHLANDER("Highlander"),
    INCORPORATOR("Incorporator"),
    INVESTOR("Investor"),
    LANDLORD("Landlord"),
    LANDSCAPER("Landscaper"),
    MAGNATE("Magnate"),
    MANUFACTURER("Manufacturer"),
    METROPOLIST("Metropolist"),
    MINER("Miner"),
    MOGUL("Mogul"),
    POLITICIAN("Politician"),
    PROMOTER("Promoter"),
    SCIENTIST("Scientist"),
    SPACE_BARON("Space Baron"),
    SUBURBIAN("Suburbian"),
    SUPPLIER("Supplier"),
    THERMALIST("Thermalist"),
    TRAVELLER("Traveller"),
    VISIONARY("Visionary"),
    ZOOLOGIST("Zoologist");

    private final String name;

    AwardEnum(final String name) {
        this.name = name;
    }

    public static AwardEnum findAwardByName(final String name) {
        for (final AwardEnum award : AwardEnum.values()) {
            if (award.getName().equals(name)) {
                return award;
            }
        }
        throw new TerraformingMarsException(Message.NO_AWARD_WAS_FOUND, name);
    }
}
