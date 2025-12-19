package dojo.supermarket.model.coupon;

import dojo.supermarket.model.discount.Discount;
import dojo.supermarket.model.interfaces.SpecialOfferStrategies;

import java.time.LocalDate;

public class CouponDiscountStrategy implements SpecialOfferStrategies.CouponOfferStrategy {

    @Override

    public Discount calculateCouponDiscount(Coupon coupon, double cartQuantity, double unitPrice, LocalDate checkoutDate) {

        if (!coupon.isValid(checkoutDate)) {
            return null;
        }

        double savings = coupon.calculateDiscountValue(cartQuantity, unitPrice);
        if(savings<=0){
            return null;
        }
        return new Discount(coupon.getProduct(), "Coupon Discount", -savings);
    }

}