package dojo.supermarket.model.coupon;

import dojo.supermarket.model.specialOffer.Discount;
import dojo.supermarket.model.specialOffer.types.SpecialOfferStrategies;

import java.time.LocalDate;

public class CouponDiscountStrategy implements SpecialOfferStrategies.CouponOfferStrategy {

    @Override

    public Discount calculateCouponDiscount(Coupon coupon, double cartQuantity, double unitPrice, LocalDate checkoutDate) {

        if (!coupon.isValid(checkoutDate)) {
            return null;
        }

        if (cartQuantity <= coupon.getQuantityNeeded()) {
            return null;
        }

        double eligibleItems = Math.min(
                cartQuantity - coupon.getQuantityNeeded(),
                coupon.getQuantityDiscounted()
        );

        double savingsPerItem = unitPrice * (1.0 - coupon.getDiscountFactor());
        double totalSavings = eligibleItems * savingsPerItem;

        return new Discount(coupon.getProduct(), "Coupon Discount", -totalSavings);
    }

}