package dojo.supermarket.model.specialOfferCalculation;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Offer;
import dojo.supermarket.model.Product;

public class TenPercentDiscountStrategy implements SpecialOfferCalculationStrategy {

    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, Offer offer) {
        double discountAmount = quantity * unitPrice * offer.argument / 100.0;
        return new Discount(product, offer.argument + "% off", -discountAmount);
    }


}