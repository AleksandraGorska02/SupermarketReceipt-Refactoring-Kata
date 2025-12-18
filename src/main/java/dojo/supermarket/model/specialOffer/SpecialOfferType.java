package dojo.supermarket.model.specialOffer;

import dojo.supermarket.model.interfaces.SpecialOfferStrategies;
import dojo.supermarket.model.specialOffer.types.*;

public enum SpecialOfferType {
    THREE_FOR_TWO(new ThreeForTwoStrategy()),
    TEN_PERCENT_DISCOUNT(new TenPercentDiscountStrategy()),
    TWO_FOR_AMOUNT(new TwoForAmountStrategy()),
    FIVE_FOR_AMOUNT(new FiveForAmountStrategy());

    private final SpecialOfferStrategies.SingleProductOfferStrategy strategy;

    SpecialOfferType(SpecialOfferStrategies.SingleProductOfferStrategy strategy) {
        this.strategy = strategy;
    }

    public SpecialOfferStrategies.SingleProductOfferStrategy getStrategy() {
        return strategy;
    }
}