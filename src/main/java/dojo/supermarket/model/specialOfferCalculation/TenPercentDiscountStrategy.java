package dojo.supermarket.model.specialOfferCalculation;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;

public class TenPercentDiscountStrategy implements SpecialOfferCalculationStrategy {

    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument) {
        double discountAmount = quantity * unitPrice * offerArgument / 100.0;
        return new Discount(product, offerArgument + "% off", -discountAmount);
    }


}