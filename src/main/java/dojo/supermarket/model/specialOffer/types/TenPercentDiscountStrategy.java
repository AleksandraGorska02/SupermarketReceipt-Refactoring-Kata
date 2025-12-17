package dojo.supermarket.model.specialOffer.types;

import dojo.supermarket.model.specialOffer.Discount;
import dojo.supermarket.model.product.Product;

public class TenPercentDiscountStrategy implements SpecialOfferCalculationStrategy {

    @Override
    public Discount calculateDiscount(Product product, double quantity, double unitPrice, double offerArgument) {
        double discountAmount = quantity * unitPrice * offerArgument / 100.0;
        return new Discount(product, offerArgument + "% off", -discountAmount);
    }


}