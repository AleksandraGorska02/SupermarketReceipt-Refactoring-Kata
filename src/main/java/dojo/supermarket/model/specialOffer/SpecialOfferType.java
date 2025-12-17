package dojo.supermarket.model.specialOffer;

import dojo.supermarket.model.bundle.BundleDiscountStrategy;
import dojo.supermarket.model.coupon.CouponDiscountStrategy;
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
    },
    COUPON_OFFER {
        @Override
        public SpecialOfferCalculationStrategy getStrategy() {
            return new CouponDiscountStrategy();
        }
    };

    public abstract SpecialOfferCalculationStrategy getStrategy();
}
