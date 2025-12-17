package dojo.supermarket.model.specialOffer;

import dojo.supermarket.model.specialOffer.types.*;

public enum SpecialOfferType {
    THREE_FOR_TWO {
        @Override
        public SpecialOfferCalculationStrategy getStrategy() {
            return new ThreeForTwoStrategy();
        }
    },
    TEN_PERCENT_DISCOUNT {
        @Override
        public SpecialOfferCalculationStrategy getStrategy() {
            return new TenPercentDiscountStrategy();
        }
    },
    TWO_FOR_AMOUNT {
        @Override
        public SpecialOfferCalculationStrategy getStrategy() {
            return new TwoForAmountStrategy();
        }
    },
    FIVE_FOR_AMOUNT {
        @Override
        public SpecialOfferCalculationStrategy getStrategy() {
            return new FiveForAmountStrategy();
        }
    },
    BUNDLE_DISCOUNT {
        @Override
        public SpecialOfferCalculationStrategy getStrategy() {
            return new BundleDiscountStrategy();
        }
    };

    public abstract SpecialOfferCalculationStrategy getStrategy();
}
