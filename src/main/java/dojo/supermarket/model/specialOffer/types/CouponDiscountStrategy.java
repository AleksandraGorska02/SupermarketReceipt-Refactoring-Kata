package dojo.supermarket.model.specialOffer.types;

import dojo.supermarket.model.coupon.Coupon;
import dojo.supermarket.model.product.Product;
import dojo.supermarket.model.specialOffer.Discount;

public class CouponDiscountStrategy implements SpecialOfferCalculationStrategy {
    public Discount calculateCouponDiscount(Coupon coupon, double cartQuantity, double unitPrice) {

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

    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument) {
        return null;
    }
}